package com.example.kafka_test.dao;


import com.alibaba.fastjson.JSONArray;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


//因为这里数据从kafka取出，所以这里实现kafka监听器部分，不需要实现数据库部分


@Component
public class KafkaSendDao {













}
