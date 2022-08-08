package com.example.kafka_test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KafkaSendController {

    @GetMapping(value = "/trainDoor")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainDoor(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

        return kafkaSendService.getTrainDoor(lineNum, trainNum);
    }

    @GetMapping(value = "/trainPis")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainPis(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

        return kafkaSendService.getTrainPis(lineNum, trainNum);
    }

    @GetMapping(value = "/trainBaseInfo")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return kafkaSendService.getTrainBaseInfo(lineNum, trainNum);
    }


}
