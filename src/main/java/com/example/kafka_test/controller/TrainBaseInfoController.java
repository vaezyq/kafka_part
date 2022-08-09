package com.example.kafka_test.controller;

import com.example.kafka_test.service.TrainInfoBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrainBaseInfoController {

    @Autowired
    TrainInfoBaseService trainInfoBaseService;

    @GetMapping(value = "/trainBaseInfo")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return trainInfoBaseService.getTrainBaseInfo(lineNum, trainNum);
    }
}
