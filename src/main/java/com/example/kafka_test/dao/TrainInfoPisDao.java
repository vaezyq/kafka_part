package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class TrainInfoPisDao {
    @Autowired
    private KafkaTemplate kafkaTemplate;


    private static final String train_info_PIS = "traininfo_pis";

    //PIS
    private static final HashMap<String, String> trainInfoPis = new HashMap<>();

    public HashMap<String, String> getTrainInfoPis() {
        return trainInfoPis;
    }

    // train_info_PIS页面
    @KafkaListener(id = "", topics = train_info_PIS, groupId = "group.train_pis_2")
    public void listenerTrainInfoPis(ConsumerRecord<?, ?> record) throws IOException {
        if (trainInfoPis.containsKey("" + record.key())) {
//            System.out.println(record.key());
            trainInfoPis.replace("" + record.key(), "" + record.value());
        } else {
//            System.out.println(record.key());
            trainInfoPis.put("" + record.key(), "" + record.value());
        }
    }


}
