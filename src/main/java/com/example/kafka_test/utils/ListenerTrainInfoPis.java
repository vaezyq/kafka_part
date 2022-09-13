package com.example.kafka_test.utils;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListenerTrainInfoPis extends Thread {


    private static final String train_info_PIS = "traininfo_pis";

    //PIS
    private static final HashMap<String, Map<String,String>> trainInfoPis = new HashMap<>();

    public HashMap<String, Map<String,String>> getTrainInfoPis() {
        return trainInfoPis;
    }


    KafkaProperties kafkaProperties = new KafkaProperties();

    RecordStringProcess recordStringProcess = new RecordStringProcess();


    @Override
    public void run() {

        //空调部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_hvac = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_hvac.subscribe(Arrays.asList(train_info_PIS));
        while (true) {
            ConsumerRecords<String, String> records_pis = kafkaConsumer_hvac.poll(500);


            for (ConsumerRecord<String, String> record : records_pis) {
                if (trainInfoPis.containsKey(record.key().toString().substring(0, 4))) {
                    trainInfoPis.replace(record.key().toString().substring(0, 4),recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                } else {
                    trainInfoPis.put(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                }
            }

//            System.out.println(trainInfoPis);
        }


    }
}
