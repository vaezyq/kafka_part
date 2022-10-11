package com.example.kafka_test.service;

import com.example.kafka_test.dto.video.LineClassifyDto;
import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TrainVideoService {

     public List<LineClassifyDto> getClassifyByParentId(LineClassifyQuery query);
}
