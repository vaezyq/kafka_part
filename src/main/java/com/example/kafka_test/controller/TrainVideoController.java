package com.example.kafka_test.controller;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import com.example.kafka_test.service.TrainInfoDoorService;
import com.example.kafka_test.service.TrainVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Statement;
import java.util.List;

@Controller
@RequestMapping(value = "/video")
public class TrainVideoController {

    @Autowired
    private TrainVideoService trainVideoService;

    @GetMapping(value = "/lineClassify")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据父节点得到分类信息")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
/**
 * Description: 
 * date: 2022/10/11 22:03
 * @author: Haodong Li
 * @since JDK 1.8
 * @param: query 
 * @return java.lang.Object
*/
    public Object getClassifyByParentId(LineClassifyQuery query){
        try {
            Object res =  trainVideoService.getClassifyByParentId(query);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }

    }
}
