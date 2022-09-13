package com.example.kafka_test.service;


import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainInfoPisDao;
import com.example.kafka_test.utils.ListenerTrainInfoPis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
public class TrainInfoPisService {


    @Autowired
    TrainInfoPisDao trainInfoPisDao;

    ListenerTrainInfoPis listenerTrainInfoPis=new ListenerTrainInfoPis();

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    public Map<String, String> getTrainPis(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
//        Map<String, String> pisMap = processKafkaRecordUtils.processTrainRecord(trainInfoPisDao.getTrainInfoPis().get(trainKey));

        Map<String, String> pisMap = trainInfoPisDao.getTrainInfoPis().get(trainKey);

        return processKafkaRecordUtils.removeKeySpace(pisMap);
    }

}
