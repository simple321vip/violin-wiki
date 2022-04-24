package com.g.estate.dao;

import com.g.estate.entity.BlogInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogFileRepo extends MongoRepository<BlogInfo,String> {

}
