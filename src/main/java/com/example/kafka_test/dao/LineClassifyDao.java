package com.example.kafka_test.dao;

import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LineClassifyDao {
    //Search All Classify
    public List<LineClassify> classifyByParentId(LineClassifyQuery query);

}
