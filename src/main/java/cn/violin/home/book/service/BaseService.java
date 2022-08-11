package cn.violin.home.book.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class BaseService {

    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;

    protected JPAQueryFactory queryFactory;


}
