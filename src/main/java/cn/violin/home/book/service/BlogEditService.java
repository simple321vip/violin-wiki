package cn.violin.home.book.service;

import cn.violin.home.book.entity.BlogInfo;
import cn.violin.home.book.entity.BlogType;
import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.io.BlogIn;
import cn.violin.home.book.io.BlogTypeIn;
import cn.violin.home.book.utils.Constant;
import cn.violin.home.book.utils.NumberEnum;
import cn.violin.home.book.vo.BlogBoxVo;
import cn.violin.home.book.vo.BlogVo;
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
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.violin.home.book.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class BlogEditService {

    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NumberService numberService;

    /**
     * bidに指向するドキュメントを取得する
     *
     * @param bid blogユニクロID
     * @return content ドキュメントのコンテント
     */
    public String getContent(String bid) {
        Criteria criteria = Criteria.where("bid").is(bid);
        Query query = Query.query(criteria);
        query.fields().include("content");
        BlogInfo document = mongoTemplate.findOne(query, BlogInfo.class);
        if (document == null) {
            return "";
        }
        return new String(document.getContent().getData(), StandardCharsets.UTF_8);
    }

    /**
     * ブログ新規作成処理
     *
     * @param input 新規作成に必要情報
     * @return content ドキュメントのコンテント
     */
    @Transactional
    public BlogVo insertContent(BlogIn input) {
        BlogInfo model = new BlogInfo();
        String bid = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG);
        model.setTitle(input.getTitle());
        model.setBtId(input.getBtId());
        model.setOrder(input.getOrder());
        model.setBid(bid);
        model.setContent(new Binary("".getBytes(StandardCharsets.UTF_8)));
        model.setUpdateDateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        BlogInfo blog = mongoTemplate.save(model);
        BlogVo vo = new BlogVo();
        vo.setBid(blog.getBid());
        vo.setTitle(blog.getTitle());
        vo.setBtId(blog.getBtId());
        vo.setContent("");
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
        Criteria criteria = Criteria.where("bid").is(input.getBid());
        Query query = Query.query(criteria);
        Update update = Update.update("bid", input.getBid());
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
     * delete process
     *
     * @param bid
     * @param btId
     */
    @Transactional
    public boolean deleteContent(String bid, String btId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("biId").is(btId));

        long count = mongoTemplate.count(query, BlogInfo.class);
        if (count <= 1) {
            return false;
        }

        Criteria criteria = Criteria.where("bid").is(bid);
        Query query2 = Query.query(criteria);
        mongoTemplate.remove(query2, BlogInfo.class);

        this.sortBlog(btId);

        return true;
    }

    /**
     * sort process
     *
     * @param btId
     */
    @Transactional
    public void sortBlog(String btId) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("biId").is(btId));
        List<BlogInfo> blogs = mongoTemplate.find(query1, BlogInfo.class);
        AtomicInteger index = new AtomicInteger(0);
        blogs.stream().sorted(Comparator.comparing(BlogInfo::getOrder)).forEach((blog) -> {
            Criteria criteria = Criteria.where("bid").is(blog.getBid());
            Query query2 = Query.query(criteria);
            Update update = Update.update("bid", blog.getBid())
                    .set(COLUMN_ORDER, index.intValue());
            index.getAndIncrement();
            mongoTemplate.updateFirst(query2, update, BlogType.class);
        });
    }

    /**
     * sort process
     *
     * @param input
     */
    @Transactional
    public void sortBlogFromFront(BlogIn[] input) {
        Stream.of(input).forEach(blog -> {
            Criteria criteria = Criteria.where("bid").is(blog.getBid());
            Query query = Query.query(criteria);
            Update update = Update.update("bid", blog.getBid())
                    .set(COLUMN_ORDER, blog.getOrder());
            mongoTemplate.updateFirst(query, update, BlogInfo.class);
        });
    }


    /**
     * ブログ新規作成処理
     *
     * @param input 新規作成に必要情報
     * @return content ドキュメントのコンテント
     */
    @Transactional
    public BlogBoxVo insertBlogType(BlogTypeIn input, String tenantId) {
        BlogType model = new BlogType();
        String btId = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BLOG_TYPE);
        model.setBtName(WORD_1);
        if (StringUtils.hasLength(input.getBtName())) {
            model.setBtName(input.getBtName());
        }
        model.setBtId(btId);
        model.setOrder(input.getOrder());
        model.setOwner(tenantId);
        model.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        BlogType blogType = mongoTemplate.save(model);

        BlogVo vo = this.insertContent(
                BlogIn.builder()
                        .btId(btId)
                        .title(LocalDate.now().format(Constant.FORMATTER_DATE))
                        .order(0)
                        .build()
        );
        List<BlogVo> vos = new ArrayList<>(1);
        vos.add(vo);
        BlogBoxVo box = BlogBoxVo.builder().btId(blogType.getBtId())
                .btName(blogType.getBtName())
                .order(blogType.getOrder())
                .blogVoList(vos).build();
        return box;
    }

    /**
     * ブログタイプ修正
     *
     * @param input ブログタイプ更新内容
     */
    @Transactional()
    public void updateBlogTypeName(BlogTypeIn input) {
        Criteria criteria = Criteria.where("btId").is(input.getBtId());
        Query query = Query.query(criteria);
        Update update = Update.update("btId", input.getBtId())
                .set("btName", input.getBtName());

        mongoTemplate.updateFirst(query, update, BlogType.class);
    }

    /**
     * blog type sort
     *
     * @param input to sort blog type
     */
    @Transactional()
    public void sortBlogType(BlogTypeIn[] input) {
        Stream.of(input).forEach(blogType -> {
            Criteria criteria = Criteria.where("btId").is(blogType.getBtId());
            Query query = Query.query(criteria);
            Update update = Update.update("btId", blogType.getBtId())
                    .set(COLUMN_ORDER, blogType.getOrder());
            mongoTemplate.updateFirst(query, update, BlogType.class);
        });
    }


    /**
     * 該当btIdに所属するBlogs及びBlogTypeを削除する
     *
     * @param btId BlogTypeId
     */
    @Transactional()
    public void deleteBlogType(String btId) throws Exception {
        Criteria criteria1 = Criteria.where("owner").is("xiaoguan");
        Query query1 = Query.query(criteria1);
        long count = mongoTemplate.count(query1, "t_blog_type");
        if (count > 1) {
            Criteria criteria = Criteria.where("btId").is(btId);
            Query query = Query.query(criteria);

            mongoTemplate.remove(query, "t_blog");
            mongoTemplate.remove(query, "t_blog_type");
        } else {
            throw new Exception("削除でき来ない");
        }
    }

    /**
     * to query all the blog_types and blogs
     *
     * @return List<BlogBoxVo>
     */
    @Transactional
    public List<BlogBoxVo> listAll(String id) {

        // query blog_types
        Query blogTypeQuery = new Query();
        blogTypeQuery.addCriteria(Criteria.where("owner").is(id)).with(Sort.by("order").ascending());
        List<BlogType> docs = mongoTemplate.find(blogTypeQuery, BlogType.class);

        // when the tenant login for the first time，no the blog_types exists，so to create a default blog_type。
        if (docs.isEmpty()) {
            BlogTypeIn in = new BlogTypeIn();
            in.setBtName(WORD_1);
            BlogBoxVo box = this.insertBlogType(in, id);
            List<BlogVo> blogVos = new ArrayList<>(1);
            box.setBlogVoList(blogVos);
            List<BlogBoxVo> result = new ArrayList<>(1);
            result.add(box);
            return result;
        } else {

            // to query all the blogs Not include
            List<String> btIds = docs.stream().map(BlogType::getBtId).collect(Collectors.toList());
            Query blogQuery = new Query();
            blogQuery
                    .addCriteria(Criteria.where("btId").in(btIds))
                    .with(Sort.by(COLUMN_ORDER).ascending())
                    .fields().exclude("content");
            List<BlogInfo> blogs = mongoTemplate.find(blogQuery, BlogInfo.class);

            // to build results
            List<BlogBoxVo> result = docs.stream().map(type -> {
                BlogBoxVo box = new BlogBoxVo();
                box.setBtId(type.getBtId());
                box.setBtName(type.getBtName());
                box.setOrder(type.getOrder());
                List<BlogVo> blogVos = blogs.stream().filter(doc -> doc.getBtId().equals(type.getBtId())).map(doc -> {
                    BlogVo vo = new BlogVo();
                    vo.setBid(doc.getBid());
                    vo.setTitle(doc.getTitle());
//            vo.setAutoSaveControl();
                    vo.setBtId(doc.getBtId());
                    vo.setOrder(doc.getOrder());
                    return vo;
                }).collect(Collectors.toList());
                box.setBlogVoList(blogVos);
                return box;
            }).collect(Collectors.toList());

            // to add the content of the first blog of the first blog_type

            if(!result.get(0).getBlogVoList().isEmpty()) {
                String bid = result.get(0).getBlogVoList().get(0).getBid();
                String content = this.getContent(bid);
                result.get(0).getBlogVoList().get(0).setContent(content);
            }

            return result;
        }
    }
}
