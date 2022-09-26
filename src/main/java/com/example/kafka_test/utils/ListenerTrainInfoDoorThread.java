package com.example.kafka_test.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListenerTrainInfoDoorThread extends Thread {

    //车门
    private static final HashMap<String, Map<String,String>> trainInfoDoor = new HashMap<>();

    RecordStringProcess recordStringProcess=new RecordStringProcess();

    private static final String train_info_door = "traininfo_door";

    KafkaProperties kafkaProperties = new KafkaProperties();


    public HashMap<String, Map<String,String>> getTrainInfoDoor() {
        return trainInfoDoor;
    }


    @Override
    public void run() {
        //空调部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_door = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_door.subscribe(Arrays.asList(train_info_door));
        while (true) {
            //空调部分的数据处理
            ConsumerRecords<String, String> records_door = kafkaConsumer_door.poll(500);


            for (ConsumerRecord<String, String> record : records_door) {
                if (trainInfoDoor.containsKey(record.key().toString().substring(0, 4))) {
                    trainInfoDoor.replace(record.key().toString().substring(0, 4),recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                } else {
                    trainInfoDoor.put(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                }
            }
//            System.out.println(trainInfoDoor);
        }
    }
}
