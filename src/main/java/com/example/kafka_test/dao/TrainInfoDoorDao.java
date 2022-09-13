package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainInfoDoorThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainInfoDoorDao {

//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    static ListenerTrainInfoDoorThread listenerTrainInfoDoorThread = new ListenerTrainInfoDoorThread();


    static {
        listenerTrainInfoDoorThread.start();
    }

    public HashMap<String, Map<String,String>> getTrainInfoDoor() {
        return listenerTrainInfoDoorThread.getTrainInfoDoor();
    }

//    // train_info_door页面
//    @KafkaListener(id = "", topics = train_info_door, groupId = "new_12")
//    public void listenerTrainInfoDoor(ConsumerRecord<?, ?> record) {
//        if (trainInfoDoor.containsKey("" + record.key())) {
//            trainInfoDoor.replace("" + record.key(), "" + record.value());
//        } else {
//            trainInfoDoor.put("" + record.key(), "" + record.value());
//        }
//    }


}
