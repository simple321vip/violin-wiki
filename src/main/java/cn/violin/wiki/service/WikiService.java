package cn.violin.wiki.service;

import cn.violin.common.entity.Tenant;
import cn.violin.wiki.dao.WikiBoxRepo;
import cn.violin.wiki.dao.WikiRepo;
import cn.violin.wiki.entity.Wiki;
import cn.violin.wiki.form.WikiForm;
import cn.violin.wiki.utils.NumberEnum;
import cn.violin.wiki.vo.WikiVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.violin.common.utils.CommonConstant.FORMATTER_DATETIME;
import static cn.violin.common.utils.CommonConstant.UPDATE_DATETIME;

@Service
@AllArgsConstructor
public class WikiService {

    @Autowired
    private NumberService numberService;

    @Autowired
    private WikiRepo wikiRepo;

    @Autowired
    private WikiBoxRepo wikiBoxRepo;

    /**
     * Wiki取得
     *
     * @param bid    wiki_id
     * @param tenant テナント情報
     * @return wikiVo   WIKI
     */
    public WikiVo get(String bid, Tenant tenant) throws Exception {
        Example<Wiki> queryExample
                = Example.of(Wiki.builder().tenantId(tenant.getTenantId()).bid(bid).build());

        var optional = wikiRepo.findOne(queryExample);
        if (optional.isEmpty()) {
            throw new Exception("不存在 该wiki");
        }
        var wiki = optional.get();
        var wikiVo = new WikiVo();
        BeanUtils.copyProperties(wiki, wikiVo);
        wikiVo.setBtId(wiki.getBtId());
        return wikiVo;
    }

    /**
     * Wiki一覧取得
     *
     * @param btId   wikiBox_id
     * @param tenant テナント情報
     * @return wikiVo   WIKI
     */
    public List<WikiVo> findByBtId(String btId, Tenant tenant) {
        var wikis = wikiRepo.findAllByBtId(btId, tenant.getTenantId());

        return wikis.stream().map(wiki -> {
            var wikiVo = new WikiVo();
            BeanUtils.copyProperties(wiki, wikiVo);
            return wikiVo;
        }).toList();
    }

    /**
     * WIKI作成
     *
     * @param wikiForm WIKI内容
     * @param tenant   テナント
     */
    @Transactional
    public void create(WikiForm wikiForm, Tenant tenant) throws Exception {
        var wikiBox = wikiBoxRepo.findById(wikiForm.getBtId());

        if (wikiBox.isEmpty()) {
            throw new Exception("不存在 该wikiBox");
        }

        String bid = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG);
        Wiki wiki = new Wiki();
        BeanUtils.copyProperties(wikiForm, wiki);
        wiki.setBid(bid);
        wiki.setBtId(wikiForm.getBtId());
        wiki.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        wiki.setContent("该写点什么么...");

        wikiRepo.save(wiki);
    }

    /**
     * WIKI更新
     *
     * @param wikiForm 更新内容
     */
    @Transactional
    public void update(String bid, WikiForm wikiForm, Tenant tenant) throws Exception {
        Example<Wiki> queryExample
                = Example.of(Wiki.builder().bid(bid).tenantId(tenant.getTenantId()).build());
        var optional = wikiRepo.findOne(queryExample);

        if (optional.isEmpty()) {
            throw new Exception("不存在 该wiki");
        }
        var wiki = optional.get();
        wiki.setTitle(wikiForm.getTitle());
        wiki.setContent(wikiForm.getContent());
        wikiRepo.save(wiki);
    }

    /**
     * delete process and sort process
     *
     * @param bid    删除wiki对象
     * @param tenant テナント
     */
    @Transactional
    public void delete(String bid, Tenant tenant) throws Exception {
        var wiki = wikiRepo.findByBid(bid, tenant.getTenantId());
        if (Objects.isNull(wiki)) {
            throw new Exception("不存在 该wiki");
        }

        // 件数チェック
        var count = wikiRepo.count(wiki.getBtId(), tenant.getTenantId());
        if (count == 1) {
            throw new Exception("削除でき来ない");
        }
        wikiRepo.deleteById(bid);

        // order更新
        var updateWikiList = wikiRepo.findAllByBtIdAndOrder(wiki.getBtId(), tenant.getTenantId(), wiki.getOrder());
        updateWikiList.forEach(updateWiki -> {
            updateWiki.setOrder(wiki.getOrder() - 1);
        });
        wikiRepo.saveAll(updateWikiList);
    }

    /**
     * データ処理
     *
     * @param btId WIKI_BOX ID
     * @param bIds ソートデータ
     */
    @Transactional
    public void sortWiki(String btId, String[] bIds, Tenant tenant) throws Exception {
        var wikis = wikiRepo.findAllByBtId(btId, tenant.getTenantId());
        if (wikis.size() != bIds.length) {
            throw new Exception("データ不備のため、ソートできません");
        }

        for (int order = 0; order < bIds.length; order++) {
            int finalOrder = order;
            wikis.forEach(wikiBox -> {
                if (wikiBox.getBid().equals(bIds[finalOrder])) {
                    wikiBox.setOrder(finalOrder);
                }
            });
        }
        wikiRepo.saveAll(wikis);
    }
}
