package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerDduThread;
import com.example.kafka_test.utils.ListenerTrainInfoHvacThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.plaf.IconUIResource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

@Component
public class DduDao {


//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    //  ddu信息

    static ListenerDduThread listenerDduThread = new ListenerDduThread();

    static {
        listenerDduThread.start();
    }

    public HashMap<String, String> getResDdu() {
        return listenerDduThread.getResDdu();
    }


//     ddu页面
//    @KafkaListener(id = "", topics = topic_ddu, groupId = "new_12" )
//    public void listenerDdu(ConsumerRecord<?, ?> record) {
//        if (resDdu.containsKey("" + record.key())) {
//            resDdu.replace("" + record.key(), "" + record.value());
//        } else {
//            resDdu.put("" + record.key(), "" + record.value());
//        }
//    }

}
