package cn.violin.wiki.service;

import cn.violin.wiki.entity.BlogSeq;
import cn.violin.wiki.dao.BlogSeqRepo;
import cn.violin.wiki.dao.BlogTypeSeqRepo;
import cn.violin.wiki.entity.BlogTypeSeq;
import cn.violin.wiki.utils.NumberEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class NumberService {

    @Autowired
    private BlogSeqRepo blogSeqRepo;
    @Autowired
    private BlogTypeSeqRepo blogTypeSeqRepo;

    @Transactional
    public String getNumberId(NumberEnum table) {

        String numberId = null;
        switch (table.value()) {

            case "t_blog":
                BlogSeq seq2 = new BlogSeq();
                numberId = String.format("%014d", blogSeqRepo.save(seq2).getBlogSeqId());
                break;
            case "t_blog_type":
                BlogTypeSeq seq3 = new BlogTypeSeq();
                numberId = String.format("%014d", blogTypeSeqRepo.save(seq3).getBlogTypeSeqId());

        }

        return numberId;
    }


}
