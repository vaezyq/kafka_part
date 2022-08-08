package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainInfoBaseDao {


    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //车辆基本详情,直接用处理好的结果：
    //最外层的map: key为车辆例如7002，value为一个map,对应每个字段的key和value
    private static final HashMap<String, Map<String, String>> trainInfoBase = new HashMap<>();

    private static final String train_info_base = "traininfo_base";


    public HashMap<String, Map<String, String>> getTrainInfoBase() {
        System.out.println(trainInfoBase);
        return trainInfoBase;
    }

    // train_info_base页面
    @KafkaListener(id = "", topics = train_info_base, groupId = "group.test_info")
    public void listenerTrainInfo(ConsumerRecord<?, ?> record) {
        if (trainInfoBase.containsKey(record.key().toString().substring(0, 4))) {
            trainInfoBase.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
        } else {
            trainInfoBase.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
        }
        System.out.println(trainInfoBase);
    }


}
