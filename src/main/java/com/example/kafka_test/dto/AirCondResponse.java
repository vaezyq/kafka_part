package com.example.kafka_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirCondResponse {

    // 空调模式信息
    private ArrayList<ArrayList<HashMap<String, String>>> airModel;

    //空调版本信息
    private ArrayList<ArrayList<HashMap<String, String>>> airEdition;

    //每个车厢的温度和状态
    private Map<String, String> trainTemAndStatus;

    //其余全部一维信息
    private Map<String,String> oneDimAirCondInfo;

    //温度等列表信息
    private Map<String, List<String>> tempList;

}
