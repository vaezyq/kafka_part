package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainInfoBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
public class TrainInfoBaseService {

    @Autowired
    TrainInfoBaseDao trainInfoBaseDao;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;


    public Map<String, String> getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
        System.out.println(trainInfoBaseDao.getTrainInfoBase());
        Map<String, String> trainInfoMap = trainInfoBaseDao.getTrainInfoBase().get(trainKey);
        return processKafkaRecordUtils.removeKeySpace(trainInfoMap);
    }


}
