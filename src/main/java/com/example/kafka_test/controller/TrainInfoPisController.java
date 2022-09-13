package com.example.kafka_test.controller;


import com.example.kafka_test.dao.TrainInfoPisDao;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainInfoPisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrainInfoPisController {

    @Autowired
    TrainInfoPisService trainInfoPisService;

    @GetMapping(value = "/trainPis")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainPis(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
//        return new MyResponseBody("200", "success", trainInfoPisService.getTrainPis(lineNum, trainNum));
        return trainInfoPisService.getTrainPis(lineNum, trainNum);
    }
}
