package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainCardThread;
import com.example.kafka_test.utils.ListenerTrainInfoHvacThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainCardDao {

//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    static ListenerTrainCardThread listenerTrainCardThread=new ListenerTrainCardThread();

    static {
        listenerTrainCardThread.start();
    }





    public HashMap<String, Map<String, String>> getResTrainCard() {
        return listenerTrainCardThread.getResTrainCard();
    }



//    // train_card页面
//    @KafkaListener(id = "", topics = topic_train_card, groupId = "new_12")
//    public void listenerCard(ConsumerRecord<?, ?> record) {
//
//        if (record.key().toString().substring(0, 4).equals("7005")) {
//            return;
//        }
//        if (resTrainCard.containsKey(record.key().toString().substring(0, 4))) {
////            String s = (String) record.value();
////            Map<String, String> jsonMap = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
////            System.out.println("jsonMap: " + jsonMap.toString());
//            resTrainCard.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        } else {
//            resTrainCard.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        }
//    }

}
