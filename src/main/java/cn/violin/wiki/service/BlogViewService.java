package cn.violin.wiki.service;

import cn.violin.wiki.task.FileExportTask;
import cn.violin.wiki.entity.BlogInfo;
import cn.violin.wiki.entity.BlogType;
import cn.violin.wiki.vo.BlogBoxVo;
import cn.violin.wiki.vo.BlogVo;
import cn.violin.common.config.DocsifyConf;
import cn.violin.common.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogViewService {

    public static final String SIDEBAR_FILENAME = "_sidebar.md";

    private final Object lock = new Object();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocsifyConf ocsifyConf;

    public List<BlogVo> selectBlogs(String btId, String keyWord, LocalDate startDay, LocalDate endDay, Tenant tenant) {
        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        if (StringUtils.hasLength(btId)) {
            criteria.and("btId").is(btId);
        }
//        if (StringUtils.hasLength(keyWord)) {
//
//        }
//        if (startDay != null) {
//
//        }
//        if (endDay != null) {
//
//        }
        Query query = Query.query(criteria);

        // select count
//        long total = mongoTemplate.count(query, BlogType.class);

        // page
//        Integer skip = tenant.getPageNo() * tenant.getPageSize();
//        query.skip(skip).limit(tenant.getPageSize());

        // sort
//        query.with(Sort.by(Sort.Order.desc("updateDateTime")));

        List<BlogType> bts = mongoTemplate.find(query, BlogType.class);
        List<String> btIds = bts.stream().map(BlogType::getBtId).collect(Collectors.toList());

        Criteria criteria2 = Criteria.where("btId").in(btIds);
        Query query2 = Query.query(criteria2);
        List<BlogInfo> docs = mongoTemplate.find(query2, BlogInfo.class);
        return docs.stream().map(doc -> BlogVo.builder()
                .bid(doc.getBid())
                .title(doc.getTitle())
                .content(new String(doc.getContent().getData(), StandardCharsets.UTF_8))
                .build()).collect(Collectors.toList());
    }

    public BlogVo selectBlog(String bid) {
        Criteria criteria2 = Criteria.where("bid").in(bid);
        Query query2 = Query.query(criteria2);
        BlogInfo doc = mongoTemplate.findOne(query2, BlogInfo.class);
        return BlogVo.builder()
                .bid(doc.getBid())
                .title(doc.getTitle())
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
        return bts.stream().map(bt -> BlogBoxVo.builder()
                .btId(bt.getBtId())
                .btName(bt.getBtName()).build()
        ).collect(Collectors.toList());
    }


    /**
     * publish
     */
    public void publishAll(Tenant tenant) {

        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);

        List<BlogType> bts = mongoTemplate.find(query, BlogType.class);
        List<String> btIds = bts.stream().map(BlogType::getBtId).collect(Collectors.toList());

        Criteria criteria2 = Criteria.where("btId").in(btIds);
        Query query2 = Query.query(criteria2);
        query2.with(Sort.by(Sort.Order.asc("order")));
        List<BlogInfo> docs = mongoTemplate.find(query2, BlogInfo.class);

        Criteria criteria3 = Criteria.where("tenantId").is(tenant.getTenantId());
        Query query3 = Query.query(criteria3);
        Tenant tenant1 = mongoTemplate.findOne(query3, Tenant.class);

        if (tenant1 == null){
            System.out.println("500 inner error");
        }

        LinkedHashMap<String, List<BlogInfo>> collect
                = docs.stream().collect(Collectors.groupingBy(BlogInfo::getBtId, LinkedHashMap::new, Collectors.toList()));

        ExecutorService es = Executors.newFixedThreadPool(4);

        Object lock = new Object();
        String wikiWorkSpace = ocsifyConf.getDOCSIFY_WORKSPACE() + tenant1.getWikiName() + File.separator;
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
                        finalWriter.write("  * [" + value.getTitle() + "](" + tenant1.getWikiName() + "/" + btId + "/" + value.getBid() + ".md" + ")");
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
     * @param bid bid
     */
    public void publish(String bid, Tenant tenant) {

        Criteria criteria = Criteria.where("bid").is(bid).andOperator(Criteria.where("owner").is(tenant.getTenantId()));
        Query query = Query.query(criteria);
        BlogInfo doc = mongoTemplate.findOne(query, BlogInfo.class);

        BufferedWriter writer;
        try {
            String filePath = ocsifyConf.getDOCSIFY_WORKSPACE() + doc.getBtId() + File.separator + doc.getBid() + ".md";
            File file = new File(filePath);
            writer = new BufferedWriter(new FileWriter(filePath + "_bk"));

            writer.write(new String(doc.getContent().getData(), StandardCharsets.UTF_8));
            writer.close();

            if (file.exists()) {
                file.delete();
            }

            File newFile = new File(filePath + "_bk");
            newFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader;
        BufferedWriter writer2;
        synchronized (lock) {
            try {
                File sidebar = new File(ocsifyConf.getDOCSIFY_WORKSPACE() + SIDEBAR_FILENAME);
                reader = new BufferedReader(new FileReader(sidebar));
                String record;
                StringBuilder builder = new StringBuilder();
                while ((record = reader.readLine()) != null) {
                    if (record.contains(bid)) {
                        record = "  * [" + doc.getTitle() + "](" + doc.getBtId() + "/" + bid + ".md" + ")";
                    }
                    builder.append(record);
                    builder.append(System.lineSeparator());
                }
                writer2 = new BufferedWriter(new FileWriter(sidebar));
                writer2.write(builder.toString());
                reader.close();
                writer2.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void putWiki(String name, Tenant tenant) {
        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);
//        Update update = Update.update("btId", input.getBtId())
//                .set("btName", input.getBtName());
//        mongoTemplate.updateFirst(query)
    }
}
