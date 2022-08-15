package com.example.kafka_test.controller;

import com.example.kafka_test.service.PublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PublicInfoController {

    @Autowired
    PublicInfoService publicInfoService;

    @GetMapping(value = "/publicInfo")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 只会返回空调的温度部分，这一部分是个列表
    public Object getTrainPublicInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        return publicInfoService.getPublicInfo(lineNum, trainNum);
    }
}
