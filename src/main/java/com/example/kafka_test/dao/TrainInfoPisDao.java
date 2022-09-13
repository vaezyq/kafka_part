package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainInfoPis;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainInfoPisDao {
//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    static ListenerTrainInfoPis listenerTrainInfoPis = new ListenerTrainInfoPis();

    static {
        listenerTrainInfoPis.start();
    }

    public HashMap<String, Map<String,String>> getTrainInfoPis() {
        return listenerTrainInfoPis.getTrainInfoPis();
    }



//    // train_info_PIS页面
//    @KafkaListener(id = "", topics = train_info_PIS, groupId = "new_12")
//    public void listenerTrainInfoPis(ConsumerRecord<?, ?> record) throws IOException {
//        if (trainInfoPis.containsKey("" + record.key())) {
////            System.out.println(record.key());
//            trainInfoPis.replace("" + record.key(), "" + record.value());
//        } else {
////            System.out.println(record.key());
//            trainInfoPis.put("" + record.key(), "" + record.value());
//        }
//    }


}
