package com.example.kafka_test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.kafka_test.dto.DataListAndLinksList;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainLinesService;
import com.example.kafka_test.utils.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TrainLinesController {

//    @Autowired
//    TrainLinesService trainLinesService;

    /**
     * 返回前端站点样式的json对象
     * @return
     */
    @GetMapping(value = "/trainLineStyle")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "得到所有站点信息")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
    public Object getTrainStation() {
        String dataListJson ="null";
        String linksListJson ="null";
        try{
            dataListJson = JsonUtils.readJsonData("./trainStationToFrontEnd.json");
            linksListJson= JsonUtils.readJsonData("./trainLineToFrontEnd.json");
        }catch (IOException e){
            e.printStackTrace();
        }
//        JSONArray array = JSON.parseArray(dataListJson);
//        for(Object jo : array){
//            System.out.println(jo);
//        }
        DataListAndLinksList dal = new DataListAndLinksList(JSON.parseArray(dataListJson),JSON.parseArray(linksListJson));
        //System.out.println(dal.getLinksList());
        return new MyResponseBody("200", "success", dal);
    }

}
