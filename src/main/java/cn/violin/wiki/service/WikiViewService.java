package cn.violin.wiki.service;

import cn.violin.common.entity.Tenant;
import cn.violin.wiki.config.PersistentVolumeClaimConfig;
import cn.violin.wiki.dao.ProfileRepo;
import cn.violin.wiki.dao.WikiBoxRepo;
import cn.violin.wiki.dao.WikiRepo;
import cn.violin.wiki.entity.QWiki;
import cn.violin.wiki.entity.Wiki;
import cn.violin.wiki.entity.WikiBox;
import cn.violin.wiki.task.FileExportTask;
import cn.violin.wiki.vo.WikiVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WikiViewService {

    @Autowired
    private WikiRepo wikiRepo;

    @Autowired
    private WikiBoxRepo wikiBoxRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProfileRepo profileRepo;


    public static final String SIDEBAR_FILENAME = "_sidebar.md";

    @Autowired
    private PersistentVolumeClaimConfig persistentVolumeClaimConfig;

    public List<WikiVo> selectBlogs(String btId, String keyWord, LocalDate startDay, LocalDate endDay, Tenant tenant) {

        List<WikiBox> bts;
        if (StringUtils.hasLength(btId)) {
            bts = List.of(wikiBoxRepo.findByBtIdAndTenantId(btId, tenant.getTenantId()));
        } else {
            bts = wikiBoxRepo.findAll(tenant.getTenantId());
        }

        QWiki qWiki = QWiki.wiki;

        List<String> btIds = bts.stream().map(WikiBox::getBtId).toList();
//
//        // wiki 查询
//        Query wikiQuery = Query.query(Criteria.where(COLUMN_WIKI_TYPE_ID).in(btIds));
//
//        // 标题 关键字 查询
//        if (StringUtils.hasLength(keyWord)) {
//            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
//            // TODO 曖昧検索
//            wikiQuery.addCriteria(Criteria.where(COLUMN_TITLE).regex(pattern));
//        }
//
//        // 更新日期区间查询
//        if (startDay != null) {
//            wikiQuery.addCriteria(Criteria.where(LAST_UPDATE_DATETIME).gte(startDay));
//        }
//        if (endDay != null) {
//            wikiQuery.addCriteria(Criteria.where(LAST_UPDATE_DATETIME).lte(endDay));
//        }
//
//        // 分页查询
//        Pageable pageable =
//                PageRequest.of(tenant.getPageNo(), tenant.getPageSize(), Sort.by(LAST_UPDATE_DATETIME).ascending());
//        wikiQuery.with(pageable);
//
//        List<Wiki> docs = mongoTemplate.find(wikiQuery, Wiki.class);
//
//
//        return docs.stream()
//                .map(doc -> WikiVo.builder().bid(doc.getBid()).title(doc.getTitle())
//                        .content(new String(doc.getContent().getData(), StandardCharsets.UTF_8)).build())
//                .collect(Collectors.toList());
        return null;
    }

    /**
     * publish
     */
    public void publishAll(Tenant tenant) throws Exception {

        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();

        var wikiBoxList = wikiBoxRepo.findAll(tenant.getTenantId());

        var wikis = wikiRepo.findAll(tenant.getTenantId());
        LinkedHashMap<String, List<Wiki>> collect =
                wikis.stream().collect(Collectors.groupingBy(Wiki::getBtId, LinkedHashMap::new, Collectors.toList()));

        ExecutorService es = Executors.newFixedThreadPool(4);

        Object lock = new Object();
        String wikiWorkSpace = persistentVolumeClaimConfig.getDOCSIFY_PVC() + profile.getName() + File.separator;
        wikis.forEach(vo -> es.submit(new FileExportTask(vo, lock, wikiWorkSpace)));

        // update _sidebar.md
        try {
            String filePath = wikiWorkSpace + SIDEBAR_FILENAME;
            File file = new File(filePath);

            BufferedWriter finalWriter = new BufferedWriter(new FileWriter(filePath + "_bk"));

            collect.forEach((btId, values) -> {
                Optional<WikiBox> wikiBoxOptional = wikiBoxList.stream().filter(bt -> bt.getBtId().equals(btId)).findFirst();
                wikiBoxOptional.ifPresent(wikiBox -> {
                    String btName = wikiBoxOptional.get().getBtName();
                    try {
                        finalWriter.write("* " + btName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                values.forEach(value -> {
                    try {
                        finalWriter.newLine();
                        finalWriter.write("  * [" + value.getTitle() + "](" + profile.getName() + "/" + btId + "/"
                                + value.getBid() + ".md" + ")");
                        finalWriter.newLine();
                    } catch (IOException e) {
                        log.error("publish error");
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
            log.error("publish error");
        }
    }

    /**
     * publish
     *
     * @param bid bid
     */
    public void publish(String bid, Tenant tenant) throws Exception {

        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();
        String tenantContext = profile.getName();

        var wiki = wikiRepo.findByBid(bid, tenant.getTenantId());

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
            writer.write(wiki.getContent());
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
            log.error("publish error");
        }
    }

    public void putWiki() {
    }

    /**
     * WIKI件数取得
     *
     * @param tenant テナント情報
     * @return 件数
     */
    public long getWikiCount(Tenant tenant) {
        return wikiRepo.count(tenant.getTenantId());
    }

}
