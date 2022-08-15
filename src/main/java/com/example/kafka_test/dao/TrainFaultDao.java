package com.example.kafka_test.dao;

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

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 车辆故障
    private static final HashMap<String, Map<String, String>> resTrainFault = new HashMap<>();

    private static final String topic_fault = "fault";

    public HashMap<String, Map<String, String>> getResTrainFault() {
        return resTrainFault;
    }

    private static final HashMap<String, String> faultTrainKey = new HashMap<>();
    public HashMap<String, String> getFaultTrainKey() {
        return faultTrainKey;
    }


    // train_fault页面
    @KafkaListener(id = "", topics = topic_fault, groupId = "group.fault_2")
    public void listenerFault(ConsumerRecord<?, ?> record) {
        if (faultTrainKey.containsKey(record.key().toString().substring(0, 4))) {
            faultTrainKey.replace(record.key().toString().substring(0, 4), "fault");
        } else {
            faultTrainKey.put(record.key().toString().substring(0, 4), "fault");
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
