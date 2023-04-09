package cn.violin.wiki.service;

import cn.violin.wiki.entity.BlogInfo;
import cn.violin.wiki.entity.BlogType;
import cn.violin.wiki.io.BlogIn;
import cn.violin.wiki.io.BlogTypeIn;
import cn.violin.wiki.io.SortData;
import cn.violin.wiki.utils.NumberEnum;
import cn.violin.wiki.vo.BlogBoxVo;
import cn.violin.wiki.vo.BlogVo;
import cn.violin.common.entity.Tenant;
import com.mongodb.client.result.UpdateResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.violin.wiki.utils.Constant.*;

import static cn.violin.common.utils.CommonConstant.*;

@Service
@RequiredArgsConstructor
public class BlogEditService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NumberService numberService;

    /**
     * ブログ新規作成処理
     *
     * @param input 新規作成に必要情報
     * @return content ドキュメントのコンテント
     */
    @Transactional
    public BlogBoxVo insertBlogType(BlogTypeIn input, Tenant tenant) {
        BlogType model = new BlogType();
        String btId = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG_TYPE);
        model.setBtName(WORD_1);
        if (StringUtils.hasLength(input.getBtName())) {
            model.setBtName(input.getBtName());
        }
        model.setBtId(btId);
        model.setOrder(input.getOrder());
        model.setOwner(tenant.getTenantId());
        model.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        BlogType blogType = mongoTemplate.save(model);

        BlogIn blogIn = new BlogIn();
        blogIn.setContent(WORD_2);
        blogIn.setBtId(btId);
        blogIn.setTitle(LocalDate.now().format(FORMATTER_DATE));
        blogIn.setOrder(0);
        BlogVo vo = this.insertContent(blogIn, tenant);
        List<BlogVo> vos = new ArrayList<>(1);
        vos.add(vo);
        return BlogBoxVo.builder().btId(blogType.getBtId()).btName(blogType.getBtName()).order(blogType.getOrder())
            .blogVoList(vos).build();
    }

    /**
     * 該当btIdに所属するBlogs及びBlogTypeを削除する after delete process, to query and return it. 1 件の場合は、削除できない。
     *
     * @param btId BlogTypeId
     */
    @Transactional()
    public List<BlogBoxVo> deleteBlogType(String btId, Tenant tenant) throws Exception {

        Query query = Query.query(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
        query.with(Sort.by(COLUMN_ORDER).ascending());
        List<BlogType> blogTypes = mongoTemplate.find(query, BlogType.class);
        if (blogTypes.size() > 1) {

            // delete
            Query query1 = Query.query(Criteria.where(COLUMN_WIKI_TYPE_ID).is(btId)
                .andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId())));

            mongoTemplate.remove(query1, "t_blog");
            mongoTemplate.remove(query1, "t_blog_type");

            String[] btIds =
                blogTypes.stream().filter(e -> !e.getBtId().equals(btId)).map(BlogType::getBtId).toArray(String[]::new);
            SortData sortData = new SortData();
            sortData.setBtIds(btIds);
            this.sortBlogType(sortData, tenant);
            return this.listAll(tenant);
        } else {
            throw new Exception("削除でき来ない");
        }
    }

    /**
     * 更新 wiki type
     *
     * @param input wiki type
     * @param tenant 租户
     */
    @Transactional()
    public void updateWikiType(BlogTypeIn input, Tenant tenant) {
        Criteria criteria = Criteria.where(COLUMN_WIKI_TYPE_ID).is(input.getBtId());
        criteria.andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
        Query query = Query.query(criteria);
        Update update = Update.update(COLUMN_WIKI_TYPE_ID, input.getBtId()).set("btName", input.getBtName());

        mongoTemplate.updateFirst(query, update, BlogType.class);
    }

    /**
     * 查询 wiki 一览，并且第一个wiki包含content
     *
     * @param tenant 租户
     * @param btId wiki 分类Id
     * @return BlogBoxVo
     */
    public BlogBoxVo getWikiList(String btId, Tenant tenant) {

        Query query = new Query();
        query
            .addCriteria(Criteria.where(COLUMN_WIKI_TYPE_ID).is(btId)
                .andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId())))
            .with(Sort.by(COLUMN_ORDER).ascending());
        List<BlogInfo> wikiList = mongoTemplate.find(query, BlogInfo.class);

        // 保留序列最小的wiki内容
        String content = new String(wikiList.get(0).getContent().getData(), StandardCharsets.UTF_8);
        List<BlogVo> wikiVoList = wikiList.stream().map(wiki -> BlogVo.builder().bid(wiki.getBid()).btId(wiki.getBtId())
            .order(wiki.getOrder()).title(wiki.getTitle()).build()).collect(Collectors.toList());
        wikiVoList.get(0).setContent(content);

        return BlogBoxVo.builder().btId(btId).blogVoList(wikiVoList).build();
    }

    /**
     * 查询 wiki type 一览
     *
     * @param tenant 用户
     * @return BlogBoxVo
     */
    public List<BlogBoxVo> getWikiTypeList(Tenant tenant) {

        Query query = new Query();
        query.addCriteria(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()))
            .with(Sort.by(COLUMN_ORDER).ascending());
        List<BlogType> wikiTypeList = mongoTemplate.find(query, BlogType.class);

        return wikiTypeList.stream().map(wikiType -> BlogBoxVo.builder().btId(wikiType.getBtId())
            .order(wikiType.getOrder()).btName(wikiType.getBtName()).build()).collect(Collectors.toList());
    }

    /**
     * blog type sort
     *
     * @param input to sort blog type
     * @param tenant 用户情报
     *
     * @return List<BlogBoxVo> wiki 种类一览
     */
    @Transactional()
    public List<BlogBoxVo> sortBlogType(SortData input, Tenant tenant) {

        for (int order = 0; order < input.getBtIds().length; order++) {
            Criteria criteria = Criteria.where(COLUMN_WIKI_TYPE_ID).is(input.getBtIds()[order]);
            criteria.andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
            Query query = Query.query(criteria);
            Update update = Update.update(COLUMN_WIKI_TYPE_ID, input.getBtIds()[order]).set(COLUMN_ORDER, order);
            mongoTemplate.updateFirst(query, update, BlogType.class);
        }

        return this.getWikiTypeList(tenant);
    }

    /**
     * bidに指向するドキュメントを取得する
     *
     * @param bid wiki_id
     * @param tenant 租户
     * @return wikiVo 维基
     */
    public BlogVo getWiki(String bid, Tenant tenant) throws Exception {
        Criteria criteria = Criteria.where(COLUMN_WIKI_ID).is(bid);
        criteria.andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
        Query query = Query.query(criteria);
        BlogInfo wiki = mongoTemplate.findOne(query, BlogInfo.class);
        if (wiki == null) {
            throw new Exception("不存在 该wiki");
        }

        String content = "";
        if (wiki.getContent() != null) {
            content = new String(wiki.getContent().getData(), StandardCharsets.UTF_8);
        }
        return BlogVo.builder().bid(wiki.getBid()).btId(wiki.getBtId()).title(wiki.getTitle()).content(content)
            .order(wiki.getOrder()).build();
    }

    /**
     * ブログ新規作成処理
     *
     * @param input 新規作成に必要情報
     * @param tenant 租户
     *
     * @return wiki wiki
     */
    @Transactional
    public BlogVo insertContent(BlogIn input, Tenant tenant) {
        BlogInfo model = new BlogInfo();
        String bid = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG);
        model.setTitle(input.getTitle());
        model.setBtId(input.getBtId());
        model.setOrder(input.getOrder());
        model.setBid(bid);
        model.setOwner(tenant.getTenantId());
        model.setContent(new Binary("该写点什么么...".getBytes(StandardCharsets.UTF_8)));
        model.setUpdateDateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        BlogInfo blog = mongoTemplate.save(model);
        BlogVo vo = new BlogVo();
        vo.setBid(blog.getBid());
        vo.setTitle(blog.getTitle());
        vo.setBtId(blog.getBtId());
        vo.setContent("该写点什么么...");
        vo.setOrder(blog.getOrder());
        return vo;
    }

    /**
     * ブログ更新処理
     *
     * @param input 更新内容
     * @return long 高新
     */
    @Transactional
    public long updateContent(BlogIn input) {
        Criteria criteria = Criteria.where(COLUMN_WIKI_ID).is(input.getBid());
        Query query = Query.query(criteria);
        Update update = Update.update(COLUMN_WIKI_ID, input.getBid());
        if (input.getTitle() != null) {
            update = update.set("title", input.getTitle());
        }
        if (input.getContent() != null) {
            update = update.set("content", new Binary(input.getContent().getBytes(StandardCharsets.UTF_8)));
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, BlogInfo.class);
        return updateResult.getMatchedCount();
    }

    /**
     * delete process and sort process
     *
     * @param deleteObject 删除wiki对象
     * @param tenant 租户对象
     *
     * @return BlogBoxVo
     */
    @Transactional
    public BlogBoxVo deleteWiki(BlogIn deleteObject, Tenant tenant) throws Exception {
        Query selectQuery = Query.query(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
        selectQuery.with(Sort.by(COLUMN_ORDER).ascending());
        selectQuery.addCriteria(Criteria.where(COLUMN_WIKI_TYPE_ID).is(deleteObject.getBtId()));

        // judge
        List<BlogInfo> wikiList = mongoTemplate.find(selectQuery, BlogInfo.class);
        if (wikiList.size() <= 1) {
            throw new Exception("削除でき来ない");
        }

        // delete
        Query deleteQuery = Query.query(Criteria.where(COLUMN_WIKI_ID).is(deleteObject.getBid()));
        mongoTemplate.remove(deleteQuery, BlogInfo.class);

        // sort
        AtomicInteger index = new AtomicInteger(0);
        List<BlogVo> blogVos =
            wikiList.stream().filter(wiki -> !wiki.getBid().equals(deleteObject.getBid())).map((blog) -> {
                Criteria criteria = Criteria.where(COLUMN_WIKI_ID).is(blog.getBid());
                Query updateQuery = Query.query(criteria);
                Update update = Update.update(COLUMN_WIKI_ID, blog.getBid()).set(COLUMN_ORDER, index.intValue());
                mongoTemplate.updateFirst(updateQuery, update, BlogInfo.class);
                blog.setOrder(index.intValue());
                index.getAndIncrement();
                return BlogVo.builder().bid(blog.getBid()).title(blog.getTitle())
                    .content(new String(blog.getContent().getData(), StandardCharsets.UTF_8)).btId(blog.getBtId())
                    .order(blog.getOrder()).build();
            }).collect(Collectors.toList());

        return BlogBoxVo.builder().blogVoList(blogVos).build();
    }

    /**
     * 将页面wiki的排序状态保存到数据库
     *
     * @param sortData 需要排序wiki
     */
    @Transactional
    public void sortWiki(SortData sortData, Tenant tenant) {

        for (int order = 0; order < sortData.getBIds().length; order++) {
            String bid = sortData.getBIds()[order];
            Criteria criteria = Criteria.where(COLUMN_WIKI_ID).is(bid);
            criteria.andOperator(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));
            Query query = Query.query(criteria);
            Update update = Update.update(COLUMN_WIKI_ID, bid).set(COLUMN_ORDER, order);
            mongoTemplate.updateFirst(query, update, BlogInfo.class);
        }
    }

    /**
     * to query all the blog_types and blogs
     *
     * @return List<BlogBoxVo>
     */
    @Transactional
    public List<BlogBoxVo> listAll(Tenant tenant) {

        // query blog_types
        List<BlogBoxVo> wikiTypeList = this.getWikiTypeList(tenant);

        // when the tenant login for the first time，no the blog_types exists，so to create a default blog_type。
        if (wikiTypeList.isEmpty()) {
            BlogTypeIn in = new BlogTypeIn();
            in.setBtName(WORD_1);
            BlogBoxVo box = this.insertBlogType(in, tenant);
            wikiTypeList.add(box);
        } else {
            String BtId = wikiTypeList.get(0).getBtId();
            BlogBoxVo vo = this.getWikiList(BtId, tenant);
            vo.setBtName(wikiTypeList.get(0).getBtName());
            wikiTypeList.set(0, vo);
        }
        return wikiTypeList;
    }

}
