package com.example.kafka_test.utils;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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

    public Map<String, String> removeKeySpace(Map<String, String> map) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet())
            res.put(entry.getKey().trim(), entry.getValue().toString().trim());
        return res;
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
                Map<String,String> map = removeKeySpace(recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                if (trainInfoDoor.containsKey(record.key().toString().substring(0, 4))) {
                    trainInfoDoor.replace(record.key().toString().substring(0, 4),map);
                } else {
                    trainInfoDoor.put(record.key().toString().substring(0, 4), map);
                }
            }
//            Iterator<String> iterator = trainInfoDoor.keySet().iterator();
//            while(iterator.hasNext()){
//                String trainNum = iterator.next();
//                Map<String,String> map = trainInfoDoor.get(trainNum);
//                String str = JSON.toJSONString(map);
//            }

            String string = JSON.toJSONString(trainInfoDoor);
            System.out.println(string);
        }
    }
}
