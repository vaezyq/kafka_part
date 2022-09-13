package com.example.kafka_test.utils;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListenerDriverOperationThread extends  Thread{

    KafkaProperties kafkaProperties=new KafkaProperties();

    private HashMap<String, Map<String,String>> driverOperationMap=new HashMap<>();

    private String driver_operation_topic="driveroperation";

    RecordStringProcess recordStringProcess=new RecordStringProcess();

    public HashMap<String, Map<String,String> > getDriverOperationMap(){
        return driverOperationMap;
    }


    @Override
    public void run(){
        //操作回溯部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_driverOperation = new KafkaConsumer<>(kafkaProperties.getProperties());
        //订阅操作回溯的topic
       kafkaConsumer_driverOperation.subscribe(Arrays.asList(driver_operation_topic));
       while (true){
           //部分的数据处理,每500ms拉取一次数据
           ConsumerRecords<String, String> records_ddu = kafkaConsumer_driverOperation.poll(500);
           for (ConsumerRecord<String, String> record : records_ddu) {
               if (driverOperationMap.containsKey(record.key().toString().substring(0, 4))) {
                   driverOperationMap.replace(record.key().toString().substring(0, 4),recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
               } else {
                   driverOperationMap.put(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
               }
           }
//           System.out.println(driverOperationMap);
       }
    }
}
