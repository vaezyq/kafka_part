package com.example.kafka_test.controller;


import com.example.kafka_test.dao.KafkaSendDao;
import com.example.kafka_test.service.KafkaSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class KafkaSendController {


    @Autowired
    KafkaSendService kafkaSendService;


    @Autowired
    KafkaSendDao kafkaSendDao;


    @GetMapping("/index")
    @ResponseBody
    // 用于测试
    public String index() {
        System.out.println("length of ddu information：" + kafkaSendDao.getResDdu().toString().length());
        return new String("test");
    }

    // 这里后续可以加上错误返回

    @GetMapping(value = "/trainHvac")
    @ResponseBody
    @CrossOrigin(origins = "*")  //跨域问题 https://blog.csdn.net/huo065000/article/details/123623353
    public Object getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return kafkaSendService.getTrainHvac(lineNum, trainNum);
    }

    @GetMapping(value = "/trainHvacTmp")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainHvacTemp(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

        return kafkaSendDao.getTemList(kafkaSendDao.getTrainInfoHvacList(), "7002");
    }


}
