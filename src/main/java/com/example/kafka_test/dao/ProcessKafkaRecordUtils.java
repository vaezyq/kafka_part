package com.example.kafka_test.dao;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProcessKafkaRecordUtils {

    public Map<String, String> processRecordAndString(String key, String record) {
        // 7002_2022-07-06 18:45:29 159
        String resDate = key.substring(5, 24);
        Map<String, String> resMap = processTrainCardHavc(record);
        resMap.put("date", resDate);
        return resMap;
    }
}
