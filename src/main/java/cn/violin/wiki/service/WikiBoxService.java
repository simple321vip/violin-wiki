package cn.violin.wiki.service;

import cn.violin.common.entity.Tenant;
import cn.violin.wiki.dao.WikiBoxRepo;
import cn.violin.wiki.dao.WikiRepo;
import cn.violin.wiki.entity.Wiki;
import cn.violin.wiki.entity.WikiBox;
import cn.violin.wiki.form.WikiBoxForm;
import cn.violin.wiki.utils.NumberEnum;
import cn.violin.wiki.vo.WikiBoxVo;
import cn.violin.wiki.vo.WikiVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.violin.common.utils.CommonConstant.*;
import static cn.violin.wiki.utils.Constant.WORD_1;
import static cn.violin.wiki.utils.Constant.WORD_2;


@Service
@AllArgsConstructor
public class WikiBoxService {

    @Autowired
    private WikiBoxRepo wikiBoxRepo;

    @Autowired
    private WikiRepo wikiRepo;

    @Autowired
    private NumberService numberService;

    @Autowired
    private WikiService wikiService;


    /**
     * WikiBox作成
     *
     * @param wikiBoxForm 登録内容
     */
    @Transactional
    public String create(WikiBoxForm wikiBoxForm, Tenant tenant) {
        WikiBox wikiBox = new WikiBox();
        String btId = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG_TYPE);
        var order = wikiBoxRepo.getMaxOrder(tenant.getTenantId());

        wikiBox.setBtName(WORD_1);
        if (StringUtils.hasLength(wikiBoxForm.getBtName())) {
            wikiBox.setBtName(wikiBoxForm.getBtName());
        }
        wikiBox.setBtId(btId);
        wikiBox.setOrder(order);
        wikiBox.setTenantId(tenant.getTenantId());
        wikiBox.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        wikiBoxRepo.save(wikiBox);

        Wiki wiki = new Wiki();
        wiki.setContent(WORD_2);
        wiki.setBtId(btId);
        wiki.setTitle(LocalDate.now().format(FORMATTER_DATE));
        wiki.setOrder(0);
        wikiRepo.save(wiki);

        return btId;
    }

    /**
     * 該当btIdに所属するBlogs及びBlogTypeを削除する after delete process, to query and return it. 1 件の場合は、削除できない。
     *
     * @param btId BlogTypeId
     */
    @Transactional()
    public void delete(String btId, Tenant tenant) throws Exception {
        if (wikiBoxRepo.exists(btId, tenant.getTenantId())) {
            var count = wikiBoxRepo.count(tenant.getTenantId());
            if (count == 1) {
                throw new Exception("削除でき来ない");
            }
            wikiBoxRepo.deleteById(btId);

            String[] btIds = wikiBoxRepo.findBtIdsWithOrder(tenant.getTenantId());
            this.sort(btIds, tenant);
            this.listAll(tenant);

        } else {
            throw new Exception("削除対象存在しません。");
        }
    }

    /**
     * 更新 wiki type
     *
     * @param btId        wikiBox id
     * @param wikiBoxForm wikiBox更新内容
     * @param tenant      テナント情報
     */
    @Transactional()
    public void update(String btId, WikiBoxForm wikiBoxForm, Tenant tenant) {
        var wikiBox = wikiBoxRepo.findByBtIdAndTenantId(btId, tenant.getTenantId());
        wikiBox.setBtName(wikiBoxForm.getBtName());
        wikiBoxRepo.save(wikiBox);
    }

    /**
     * 查询 wiki 一览，并且第一个wiki包含content
     *
     * @param tenant テナント
     * @param btId   btId
     * @return BlogBoxVo
     */
    public WikiBoxVo get(String btId, Tenant tenant) {
        var wikiVoList = wikiService.findByBtId(btId, tenant);
        return WikiBoxVo.builder().btId(btId).wikiVoList(wikiVoList).build();
    }

    /**
     * 查询 wiki type 一览
     *
     * @param tenant 用户
     * @return BlogBoxVo
     */
    public List<WikiBoxVo> getWikiTypeList(Tenant tenant) {
        var wikiBoxList = wikiBoxRepo.findAll(tenant.getTenantId());
        return wikiBoxList.stream().map(wikiBox -> WikiBoxVo.builder()
                .btId(wikiBox.getBtId())
                .order(wikiBox.getOrder())
                .btName(wikiBox.getBtName())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * blog type sort
     *
     * @param btIds  ソートデータ
     * @param tenant テナント情報
     */
    @Transactional()
    public void sort(String[] btIds, Tenant tenant) throws Exception {
        var wikiBoxList = wikiBoxRepo.findAll(tenant.getTenantId());
        if (wikiBoxList.size() != btIds.length) {
            throw new Exception("データ不備のため、ソートできません");
        }

        for (int order = 0; order < btIds.length; order++) {
            int finalOrder = order;
            wikiBoxList.forEach(wikiBox -> {
                if (wikiBox.getBtId().equals(btIds[finalOrder])) {
                    wikiBox.setOrder(finalOrder);
                }
            });
        }

        wikiBoxRepo.saveAll(wikiBoxList);
    }


    /**
     * to query all the blog_types and blogs
     *
     * @return List<BlogBoxVo>
     */
    @Transactional
    public List<WikiBoxVo> listAll(Tenant tenant) {
        List<WikiBoxVo> wikiTypeList = this.getWikiTypeList(tenant);

        // when the tenant login for the first time，no the blog_types exists，so to create a default blog_type。
        if (wikiTypeList.isEmpty()) {
            String btId = this.create(new WikiBoxForm(), tenant);
            wikiTypeList.add(this.get(btId, tenant));
        }

        var first = wikiTypeList.get(0);
        List<WikiVo> wikiVos = wikiService.findByBtId(first.getBtId(), tenant);
        first.setWikiVoList(wikiVos);

        return wikiTypeList;
    }

}
