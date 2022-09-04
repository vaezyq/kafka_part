package com.example.kafka_test.utils;

import org.springframework.stereotype.Component;

import java.util.Properties;


// 定义kafka的公共配置

public class KafkaProperties {
    // kafka的消费者组配置
    static Properties properties = new Properties();

    public static Properties getProperties() {
        return properties;
    }

    static {
        properties.put("bootstrap.servers", "47.100.176.192:9092");
        int a = (int) (1 + Math.random() * (10000 - 1 + 1));
        properties.put("group.id", "group-1" + a);
        properties.put("enable.auto.commit", "false");
//        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "latest");
//        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }
}
