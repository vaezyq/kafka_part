package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class DduDao {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    //  ddu信息
    private static final HashMap<String, String> resDdu = new HashMap<>();
    //主题
    private static final String topic_ddu = "ddu";

    // ddu页面
    @KafkaListener(id = "", topics = topic_ddu, groupId = "group.ddu")
    public void listenerDdu(ConsumerRecord<?, ?> record) {
        if (resDdu.containsKey("" + record.key())) {
            resDdu.replace("" + record.key(), "" + record.value());
        } else {
            resDdu.put("" + record.key(), "" + record.value());
        }
    }

    public HashMap<String, String> getResDdu() {
        return resDdu;
    }

}
