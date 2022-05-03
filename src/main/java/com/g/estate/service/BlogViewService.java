package com.g.estate.service;

import com.g.estate.entity.BlogInfo;
import com.g.estate.entity.BlogType;
import com.g.estate.vo.BlogBoxVo;
import com.g.estate.vo.BlogVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@NoArgsConstructor
@RequiredArgsConstructor
public class BlogViewService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<BlogVo> selectBlogs(String btId, String keyWord, LocalDate startDay, LocalDate endDay) {
        String owner = "xiaoguan";
        Criteria criteria = Criteria.where("owner").is(owner);
        if (StringUtils.hasLength(btId)) {
            criteria.and("btId").is(btId);
        }
        if (StringUtils.hasLength(keyWord)) {

        }
        if (startDay != null) {

        }
        if (endDay != null) {

        }
        Query query = Query.query(criteria);
        List<BlogType> bts = mongoTemplate.find(query, BlogType.class);
        List<String> btIds = bts.stream().map(BlogType::getBtId).collect(Collectors.toList());

        Criteria criteria2 = Criteria.where("btId").in(btIds);
        Query query2 = Query.query(criteria2);
        List<BlogInfo> docs = mongoTemplate.find(query2, BlogInfo.class);
        return docs.stream().map(doc -> BlogVo.builder()
                .bid(doc.getBid())
                .title(doc.getTitle())
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
                                     * @return btNameリスト
                                     */
    public List<BlogBoxVo> selectBtName() {
        String owner = "xiaoguan";
        Criteria criteria = Criteria.where("owner").is(owner);
        Query query = Query.query(criteria);

        List<BlogType> bts = mongoTemplate.find(query, BlogType.class);
        List<BlogBoxVo> boxVos = bts.stream().map(bt -> BlogBoxVo.builder()
                .btId(bt.getBtId())
                .btName(bt.getBtName()).build()
        ).collect(Collectors.toList());
        return boxVos;
    }
}
