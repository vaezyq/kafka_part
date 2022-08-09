package com.example.kafka_test.controller;


import com.example.kafka_test.service.TrainInfoDoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrainInfoDoorController {


    @Autowired
    TrainInfoDoorService trainInfoDoorService;

    @GetMapping(value = "/trainDoor")
    @ResponseBody
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainDoor(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return trainInfoDoorService.getTrainDoor(lineNum, trainNum);
    }
}
