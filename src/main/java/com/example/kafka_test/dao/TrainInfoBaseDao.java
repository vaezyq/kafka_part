package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainBaseInfoThread;
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


    static ListenerTrainBaseInfoThread listenerTrainBaseInfoThread = new ListenerTrainBaseInfoThread();

    static {
        listenerTrainBaseInfoThread.start();
    }

    public HashMap<String, Map<String, String>> getTrainInfoBase() {
        return listenerTrainBaseInfoThread.getTrainInfoBase();
    }

//    // train_info_base页面
//    @KafkaListener(id = "", topics = train_info_base, groupId = "new_12")
//    public void listenerTrainInfo(ConsumerRecord<?, ?> record) {
//        if (trainInfoBase.containsKey(record.key().toString().substring(0, 4))) {
//            trainInfoBase.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        } else {
//            trainInfoBase.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        }
//    }


}
