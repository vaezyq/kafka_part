package com.example.kafka_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testController {
    @GetMapping("/index")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 用于测试
    public String index() {
        return new String("test");
    }
}
