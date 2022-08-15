package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainInfoBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrainInfoBaseService {

    @Autowired
    TrainInfoBaseDao trainInfoBaseDao;

    @Autowired
    TrainInfoHvacService trainInfoHvacService;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;


    public Map<String, String> getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
//        String trainKey = "7002";

        String trainKey = getTrainKey(lineNum, trainNum);
//        System.out.println(trainKey);
//        System.out.println(trainInfoBaseDao.getTrainInfoBase());
        if (trainKey.length() != 4) {
            return new HashMap<>();
        }

        Map<String, String> trainInfoMap = trainInfoBaseDao.getTrainInfoBase().get(trainKey);
        if (trainInfoMap == null) return new HashMap<>();
        return processKafkaRecordUtils.removeKeySpace(trainInfoMap);
    }

    public String getTrainKey(String lineNum, String trainNum) {
        String trainKey = "";
        if (lineNum.length() == 1) {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "00" + trainNum;
            } else {
                trainKey = lineNum + "0" + trainNum;
            }
        } else {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "0" + trainNum;
            } else {
                trainKey = lineNum + trainNum;
            }
        }
        return trainKey;
    }


}
