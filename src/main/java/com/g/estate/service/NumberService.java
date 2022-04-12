package com.g.estate.service;

import com.g.estate.dao.BlogSeqRepo;
import com.g.estate.dao.BlogTypeSeqRepo;
import com.g.estate.dao.BookmarkSeqRepo;
import com.g.estate.entity.Blog;
import com.g.estate.entity.BlogSeq;
import com.g.estate.entity.BlogTypeSeq;
import com.g.estate.entity.BookmarkSeq;
import com.g.estate.utils.NumberEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberService {

    @Autowired
    private BookmarkSeqRepo bookmarkSeqRepo;
    @Autowired
    private BlogSeqRepo blogSeqRepo;
    @Autowired
    private BlogTypeSeqRepo blogTypeSeqRepo;

    public String getNumberId(NumberEnum table) {

        String numberId = null;
        switch (table.value()) {

            case "t_bookmark":
                BookmarkSeq seq1 = new BookmarkSeq();
                numberId = String.format("%014d", bookmarkSeqRepo.save(seq1).getBkSeqId());
                break;
            case "t_blog":
                BlogSeq seq2 = new BlogSeq();
                numberId = String.format("%014d", blogSeqRepo.save(seq2).getBlogSeqId());
            case "t_blog_type":
                BlogTypeSeq seq3 = new BlogTypeSeq();
                numberId = String.format("%014d", blogTypeSeqRepo.save(seq3).getBlogTypeSeqId());

        }

        return numberId;
    }


}
