package com.example.kafka_test.dao;


import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class testKafka {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "47.100.176.192:9092");
        int a = (int) (1 + Math.random() * (10000 - 1 + 1));
        properties.put("group.id", "group-1" + a);
        properties.put("enable.auto.commit", "false");
//        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "latest");
//        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("ddu"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

    }
}
