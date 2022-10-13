package com.example.kafka_test.service;

import com.example.kafka_test.dto.video.LineClassifyDto;
import com.example.kafka_test.dto.video.LineVideoConfigDto;
import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.LineVideoConfig;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import com.example.kafka_test.pojo.query.LineVideoConfigUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TrainVideoService {

     public List<LineClassifyDto> getClassifyByParentId(LineClassifyQuery query);

     public List<LineVideoConfig> getLineVideoConfig(Integer id1, Integer id2, Integer id3, Integer id4,String place);

     public List<LineVideoConfigDto> insertLineVideoConfig(List<LineVideoConfigDto> list);
     public List<LineVideoConfigDto> updateLineVideoConfig(List<LineVideoConfigDto> list);
     public Integer deleteLineVideoConfig(List<Integer> list);
}
