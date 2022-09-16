package com.example.kafka_test.utils;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ListenerTrainFaultThread extends Thread {

    // 车辆故障
    private static final HashMap<String, Map<String, String>> resTrainFault = new HashMap<>();

    private static final String topic_fault = "fault";

    private static final HashMap<String, String> faultTrainKey = new HashMap<>();


    public HashMap<String, Map<String, String>> getResTrainFault() {
        return resTrainFault;
    }

    public HashMap<String, String> getFaultTrainKey() {
        return faultTrainKey;
    }

    KafkaProperties kafkaProperties = new KafkaProperties();


    @Override
    public void run() {

        //卡片部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_fault = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_fault.subscribe(Arrays.asList(topic_fault));
        while (true) {
            ConsumerRecords<String, String> records_fault = kafkaConsumer_fault.poll(500);
            for (ConsumerRecord<String, String> record : records_fault) {
                if (faultTrainKey.containsKey(record.key().toString().substring(0, 4))) {
                    faultTrainKey.replace(record.key().toString().substring(0, 4), "fault");
                } else {
                    faultTrainKey.put(record.key().toString().substring(0, 4), "fault");
                }

//                System.out.println(record.value());
            }
//            System.out.println(faultTrainKey);
        }
    }

    //故障的字符串处理，得到hasmap类型
    public Map<String, String> processFaultStr(HashMap<String, String> resTrainFault) {
        Map<String, String> faultLevel = new HashMap<>();
        Iterator<String> iterator = resTrainFault.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String s = resTrainFault.get(key);
            if (s.contains("重大故障")) {
                faultLevel.put(key, "fault");
            } else if (s.contains("中度故障")) {
                faultLevel.put(key, "fault");
            } else {
                faultLevel.put(key, "fault");
            }
        }
        return faultLevel;
    }

}
