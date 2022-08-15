package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TrainInfoDoorDao {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //车门
    private static final HashMap<String, String> trainInfoDoor = new HashMap<>();

    private static final String train_info_door = "traininfo_door";

    // train_info_door页面
    @KafkaListener(id = "", topics = train_info_door, groupId = "group.train_door_2")
    public void listenerTrainInfoDoor(ConsumerRecord<?, ?> record) {
        if (trainInfoDoor.containsKey("" + record.key())) {
            trainInfoDoor.replace("" + record.key(), "" + record.value());
        } else {
            trainInfoDoor.put("" + record.key(), "" + record.value());
        }
    }

    public HashMap<String, String> getTrainInfoDoor() {
        return trainInfoDoor;
    }

}
