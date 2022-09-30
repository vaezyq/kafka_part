package com.example.kafka_test.controller;

import com.example.kafka_test.dao.TrainInfoHvacDao;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


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
    public Object getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) throws ParseException {

//        return new MyResponseBody("200", "success", trainInfoHvacService.getTrainHvac(lineNum, trainNum));

        Map<String, String> res = new HashMap<>();

        System.out.println(trainInfoHvacService.getAirCondResult(lineNum, trainNum));
//        for (Map.Entry<String, String> entry : trainInfoHvacService.getAirCondResult(lineNum,trainNum).entrySet()) {
//            if (entry.getKey().indexOf(" ") == 0) {  //空格都是开头第一个
//                res.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
//            } else {
//                System.out.println(entry.getKey());
//                System.out.printf(entry.getValue());
////                res.put(entry.getKey(), entry.getValue().toString());
//            }
//        }
        return new MyResponseBody("200", "success", trainInfoHvacService.getAirCondResult(lineNum, trainNum));
//        return trainInfoHvacService.getTrainHvac(lineNum, trainNum);
    }

    @GetMapping(value = "/trainHvacTmp")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainHvacTemp(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) throws ParseException {

        String trainKey = trainInfoHvacService.getTrainKey(lineNum, trainNum);
        return new MyResponseBody("200", "success", trainInfoHvacDao.getTemList(trainInfoHvacDao.getTrainInfoHvacList(), trainKey));
//        return trainInfoHvacDao.getTemList(trainInfoHvacDao.getTrainInfoHvacList(), "7002");
    }

}
