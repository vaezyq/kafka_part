package com.example.kafka_test.controller;

import com.example.kafka_test.dao.TrainInfoHvacDao;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class TrainInfoHvacController {

    @Autowired
    TrainInfoHvacService trainInfoHvacService;

    @Autowired
    TrainInfoHvacDao trainInfoHvacDao;

    // 这里后续可以加上错误返回
    @GetMapping(value = "/trainHvac")
    @ResponseBody
    @CrossOrigin(origins = "*")  //跨域问题 https://blog.csdn.net/huo065000/article/details/123623353
    public Object getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return trainInfoHvacService.getTrainHvac(lineNum, trainNum);
    }

    @GetMapping(value = "/trainHvacTmp")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainHvacTemp(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

        return trainInfoHvacDao.getTemList(trainInfoHvacDao.getTrainInfoHvacList(), "7002");
    }

}
