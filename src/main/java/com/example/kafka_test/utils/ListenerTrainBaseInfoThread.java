package com.example.kafka_test.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListenerTrainBaseInfoThread extends Thread{

    KafkaProperties kafkaProperties=new KafkaProperties();

    RecordStringProcess recordStringProcess=new RecordStringProcess();

    //车辆基本详情,直接用处理好的结果：
    //最外层的map: key为车辆例如7002，value为一个map,对应每个字段的key和value
    private static final HashMap<String, Map<String, String>> trainInfoBase = new HashMap<>();

    private static final String train_info_base = "traininfo_base";

    public HashMap<String, Map<String, String>> getTrainInfoBase(){
        return trainInfoBase;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> kafkaConsumer_baseInfo = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_baseInfo.subscribe(Arrays.asList(train_info_base));
        while (true){
            ConsumerRecords<String, String> records_base_info = kafkaConsumer_baseInfo.poll(500);
            for (ConsumerRecord<String, String> record : records_base_info) {
                if (trainInfoBase.containsKey(record.key().toString().substring(0, 4))) {
                    trainInfoBase.replace(record.key().toString().substring(0, 4),recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                } else {
                    trainInfoBase.put(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                }
            }
//            System.out.println(trainInfoBase);
        }
    }
}
