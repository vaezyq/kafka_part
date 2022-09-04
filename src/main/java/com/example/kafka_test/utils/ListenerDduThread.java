package com.example.kafka_test.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListenerDduThread extends Thread {

    @Autowired
    KafkaProperties kafkaProperties;

    // ddu部分需要使用的数据字段
    private static final HashMap<String, String> resDdu = new HashMap<>();

    //主题
    private static final String topic_ddu = "ddu";

    public HashMap<String, String> getResDdu() {
        return resDdu;
    }

    @Override
    public void run() {
        //ddu部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_ddu = new KafkaConsumer<>(kafkaProperties.getProperties());
        //订阅ddu的topic
        kafkaConsumer_ddu.subscribe(Arrays.asList(topic_ddu));
        while (true) {
            //ddu部分的数据处理,每500ms拉取一次数据
            ConsumerRecords<String, String> records_ddu = kafkaConsumer_ddu.poll(500);
            for (ConsumerRecord<String, String> record : records_ddu) {
                //ddu部分的key应该也是列车号+时间的方式，这里后续需要更改
                if (resDdu.containsKey("" + record.key())) {
                    resDdu.replace("" + record.key(), "" + record.value());
                } else {
                    resDdu.put("" + record.key(), "" + record.value());
                }
            }
//            System.out.println(resDdu);
        }
    }
}
