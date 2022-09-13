package com.example.kafka_test;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableSwagger2
@SpringBootApplication
@RestController
@RequestMapping
public class KafkaTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
    }

}
