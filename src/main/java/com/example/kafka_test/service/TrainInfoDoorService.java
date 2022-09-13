package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainInfoDoorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrainInfoDoorService {

    @Autowired
    TrainInfoDoorDao trainInfoDoorDao;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;
    public final static Map<String, String> res_without_blank = new HashMap<>();

    public Map<String, String> getTrainDoor(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
        Map<String, String> doorMap = trainInfoDoorDao.getTrainInfoDoor().get(trainKey);

        for (Map.Entry<String, String> entry : doorMap.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {
                res_without_blank.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                res_without_blank.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return res_without_blank;
    }

}
