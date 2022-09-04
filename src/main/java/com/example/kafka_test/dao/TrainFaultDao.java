package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainCardThread;
import com.example.kafka_test.utils.ListenerTrainFaultThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class TrainFaultDao {

    static ListenerTrainFaultThread listenerTrainFaultThread = new ListenerTrainFaultThread();


    static {
        listenerTrainFaultThread.start();
    }

    public HashMap<String, Map<String, String>> getResTrainFault() {
        return listenerTrainFaultThread.getResTrainFault();
    }

    public HashMap<String, String> getFaultTrainKey() {

        return listenerTrainFaultThread.getFaultTrainKey();
    }


//    // train_fault页面
//    @KafkaListener(id = "", topics = topic_fault, groupId = "new_12")
//    public void listenerFault(ConsumerRecord<?, ?> record) {
//        if (faultTrainKey.containsKey(record.key().toString().substring(0, 4))) {
//            faultTrainKey.replace(record.key().toString().substring(0, 4), "fault");
//        } else {
//            faultTrainKey.put(record.key().toString().substring(0, 4), "fault");
//        }
//    }


}
