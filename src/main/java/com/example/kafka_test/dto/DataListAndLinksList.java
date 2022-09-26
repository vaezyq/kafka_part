package com.example.kafka_test.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;

public class DataListAndLinksList {
    private JSONArray dataList;
    private JSONArray linksList;

    public DataListAndLinksList(){

    }

    public DataListAndLinksList(JSONArray dataList, JSONArray linksList) {
        this.dataList = dataList;
        this.linksList = linksList;
    }

    public JSONArray getDataList() {
        return dataList;
    }

    public void setDataList(JSONArray dataList) {
        this.dataList = dataList;
    }

    public JSONArray getLinksList() {
        return linksList;
    }

    public void setLinksList(JSONArray linksList) {
        this.linksList = linksList;
    }
}
