package cn.violin.wiki.service;

import cn.violin.wiki.entity.Profile;
import cn.violin.wiki.task.FileExportTask;
import cn.violin.wiki.entity.BlogInfo;
import cn.violin.wiki.entity.BlogType;
import cn.violin.wiki.vo.BlogBoxVo;
import cn.violin.wiki.vo.BlogVo;
import cn.violin.common.config.PersistentVolumeClaimConfig;
import cn.violin.common.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.violin.common.utils.CommonConstant.LAST_UPDATE_DATETIME;
import static cn.violin.wiki.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class WikiViewService {

    public static final String SIDEBAR_FILENAME = "_sidebar.md";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PersistentVolumeClaimConfig persistentVolumeClaimConfig;

    public List<BlogVo> selectBlogs(String btId, String keyWord, LocalDate startDay, LocalDate endDay, Tenant tenant) {

        // wiki type 查询
        Query wikiTypeQuery = new Query();

        // 默认 组合ID 查询。
        wikiTypeQuery.addCriteria(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));

        // 指定 wiki 种别 查询
        if (StringUtils.hasLength(btId)) {
            wikiTypeQuery.addCriteria(Criteria.where(COLUMN_WIKI_TYPE_ID).is(btId));
        }

        List<BlogType> bts = mongoTemplate.find(wikiTypeQuery, BlogType.class);
        List<String> btIds = bts.stream().map(BlogType::getBtId).collect(Collectors.toList());

        // wiki 查询
        Query wikiQuery = Query.query(Criteria.where(COLUMN_WIKI_TYPE_ID).in(btIds));

        // 标题 关键字 查询
        if (StringUtils.hasLength(keyWord)) {
            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
            // TODO 曖昧検索
            wikiQuery.addCriteria(Criteria.where(COLUMN_TITLE).regex(pattern));
        }

        // 更新日期区间查询
        if (startDay != null) {
            wikiQuery.addCriteria(Criteria.where(LAST_UPDATE_DATETIME).gte(startDay));
        }
        if (endDay != null) {
            wikiQuery.addCriteria(Criteria.where(LAST_UPDATE_DATETIME).lte(endDay));
        }

        // 分页查询
        Pageable pageable =
            PageRequest.of(tenant.getPageNo(), tenant.getPageSize(), Sort.by(LAST_UPDATE_DATETIME).ascending());
        wikiQuery.with(pageable);

        List<BlogInfo> docs = mongoTemplate.find(wikiQuery, BlogInfo.class);

        return docs.stream()
            .map(doc -> BlogVo.builder().bid(doc.getBid()).title(doc.getTitle())
                .content(new String(doc.getContent().getData(), StandardCharsets.UTF_8)).build())
            .collect(Collectors.toList());

    }

    public BlogVo selectBlog(String bid) {
        Criteria criteria2 = Criteria.where(COLUMN_WIKI_ID).in(bid);
        Query query2 = Query.query(criteria2);
        BlogInfo doc = mongoTemplate.findOne(query2, BlogInfo.class);
        return BlogVo.builder().bid(doc.getBid()).title(doc.getTitle())
            .content(new String(doc.getContent().getData(), StandardCharsets.UTF_8)).build();

    }

    /**
     * 該当ユーザに所属するbtNameリストを取得する
     *
     * @return btNameリスト
     */
    public List<BlogBoxVo> selectBtName(Tenant tenant) {
        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);

        List<BlogType> bts = mongoTemplate.find(query, BlogType.class);
        return bts.stream().map(bt -> BlogBoxVo.builder().btId(bt.getBtId()).btName(bt.getBtName()).build())
            .collect(Collectors.toList());
    }

    /**
     * publish
     */
    public void publishAll(Tenant tenant) throws Exception {

        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);

        List<BlogType> bts = mongoTemplate.find(query.with(Sort.by(Sort.Order.asc(COLUMN_ORDER))), BlogType.class);
        List<String> btIds = bts.stream().map(BlogType::getBtId).collect(Collectors.toList());

        Criteria criteria2 = Criteria.where(COLUMN_WIKI_TYPE_ID).in(btIds);
        Query query2 = Query.query(criteria2);
        query2.with(Sort.by(Sort.Order.asc(COLUMN_ORDER)));
        List<BlogInfo> docs = mongoTemplate.find(query2, BlogInfo.class);

        // 租户上下文存在确认
        Profile profile = mongoTemplate.findOne(query, Profile.class);
        if (profile == null) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }

        LinkedHashMap<String, List<BlogInfo>> collect =
            docs.stream().collect(Collectors.groupingBy(BlogInfo::getBtId, LinkedHashMap::new, Collectors.toList()));

        ExecutorService es = Executors.newFixedThreadPool(4);

        Object lock = new Object();
        String wikiWorkSpace = persistentVolumeClaimConfig.getDOCSIFY_PVC() + profile.getName() + File.separator;
        docs.forEach(vo -> es.submit(new FileExportTask(vo, lock, wikiWorkSpace)));

        // update _sidebar.md
        try {
            String filePath = wikiWorkSpace + SIDEBAR_FILENAME;
            File file = new File(filePath);

            BufferedWriter finalWriter = new BufferedWriter(new FileWriter(filePath + "_bk"));

            collect.forEach((btId, values) -> {
                Optional<BlogType> blogType = bts.stream().filter(bt -> bt.getBtId().equals(btId)).findFirst();
                String btName = blogType.get().getBtName();
                try {
                    finalWriter.write("* " + btName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                values.forEach(value -> {
                    try {
                        finalWriter.newLine();
                        finalWriter.write("  * [" + value.getTitle() + "](" + profile.getName() + "/" + btId + "/"
                            + value.getBid() + ".md" + ")");
                        finalWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            finalWriter.close();
            if (file.exists()) {
                file.delete();
            }
            File newFile = new File(filePath + "_bk");
            newFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * publish
     * 
     * @param bid bid
     */
    public void publish(String bid, Tenant tenant) throws Exception {

        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);

        // 租户上下文存在确认
        Profile profile = mongoTemplate.findOne(query, Profile.class);
        if (profile == null) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }

        String tenantContext = profile.getName();

        // Wiki情報 取得
        Criteria criteria1 = Criteria.where(COLUMN_WIKI_ID).is(bid)
            .andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
        Query query1 = Query.query(criteria1);
        BlogInfo wiki = mongoTemplate.findOne(query1, BlogInfo.class);
        assert wiki != null;

        // Wiki 发布
        BufferedWriter writer;
        try {

            // /docsify/docs/docs/guan/001/00001.md
            String filePath = persistentVolumeClaimConfig.getDOCSIFY_PVC() + tenantContext + File.separator + wiki.getBtId()
                + File.separator + wiki.getBid() + ".md";
            File file = new File(filePath);

            // 如果 wiki 文件存在 则将该wiki文件重命名为 wiki_bk，然后创建新的wiki文件，成功后则删除_bk文件。
            // 如果 wiki 文件不存在， 则创建新的wiki 文件。
            if (file.exists()) {
                if (!file.renameTo(new File(filePath + "_bk"))) {
                    throw new Exception("Wiki 发布 ERROR，无法重命名WIKI文件。");
                }
            }

            // 将发布内容写入 Wiki 文件
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(new String(wiki.getContent().getData(), StandardCharsets.UTF_8));
            writer.close();

            // 将 wiki title 更新到 sidebar.md
            BufferedReader reader;
            BufferedWriter writer2;
            File sidebar =
                new File(persistentVolumeClaimConfig.getDOCSIFY_PVC() + tenantContext + File.separator + SIDEBAR_FILENAME);
            reader = new BufferedReader(new FileReader(sidebar));
            String record;
            StringBuilder builder = new StringBuilder();
            while ((record = reader.readLine()) != null) {
                if (record.contains(bid)) {
                    record = "  * [" + wiki.getTitle() + "](" + wiki.getBtId() + "/" + bid + ".md" + ")";
                }
                builder.append(record);
                builder.append(System.lineSeparator());
            }
            writer2 = new BufferedWriter(new FileWriter(sidebar));
            writer2.write(builder.toString());
            reader.close();
            writer2.close();

            File deleteFile = new File(filePath + "_bk");
            if (deleteFile.exists()) {
                if (!deleteFile.delete()) {
                    throw new Exception("Wiki_bk 删除 ERROR，无法删除WIKI_bk文件。");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putWiki() {}

    public long getWikiCount(Tenant tenant) {
        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);

        return mongoTemplate.count(query, BlogInfo.class);
    }

}
