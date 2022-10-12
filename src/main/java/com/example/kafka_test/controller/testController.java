package com.example.kafka_test.controller;

import com.example.kafka_test.dao.ProcessLineDataUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class testController {

    @ApiOperation(value = "测试后端是否正常运行")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = String.class)})
    @GetMapping("/index")
    @ResponseBody
    @CrossOrigin(origins = "*")
    // 用于测试
    public String index() throws Exception {

        ProcessLineDataUtils processLineDataUtils = new ProcessLineDataUtils();
        processLineDataUtils.getTrainLineJson("123");
        return "123";


    }
}
