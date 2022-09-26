package com.example.kafka_test.service;

import com.alibaba.fastjson.JSON;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dto.TrainLine;
import com.example.kafka_test.dto.TrainStation;
import com.example.kafka_test.utils.JsonUtils;
import com.sun.javafx.css.CalculatedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainLinesService {

    @Autowired
    TrainCardDao trainCardDao;

    List<TrainStation> trainStationList;

    List<TrainLine> trainLineList;

    public List<TrainLine> getTrainLineList() {
        return trainLineList;
    }

    public void setTrainLineList(List<TrainLine> trainLineList) {
        this.trainLineList = trainLineList;
    }

    public List<TrainStation> getTrainStationList() {
        return trainStationList;
    }

    public void setTrainStationList(List<TrainStation> trainStationList) {
        this.trainStationList = trainStationList;
    }

    //无参构造方法，初始化trainLocationMap和trainLineIntegerHashMap;
    public TrainLinesService() {
        this.trainStationList=getTrainListFromJson();
        this.trainLineList=getTrainLineFromJson();
    }

    /**
     * 从json文件中获取列车名称和坐标构成的list
     * @return
     */
    private List<TrainStation> getTrainListFromJson(){
        String json = "null";
        try{
            json= JsonUtils.readJsonData("src/main/resources/static/TrainStationList.json");
        }catch (IOException e){
            e.printStackTrace();
        }
        //将json数组转化为TrainStation对象集合
        List<TrainStation> listTrainStation = JSON.parseArray(json,TrainStation.class);
        return listTrainStation;
    }

    /**
     * 从json文件中获取列车前后站和距离构成的map
     * hashmap的键是TrainLine，封装着来自json文件中的 target source 和 distance
     * 当从车辆卡片中获取到target和source时，封装成一个TrainLine对象，distance为空
     * 由于重写了equals对象，就可以从hashmap中获取到两站之间的距离
     * @return
     */
    private List<TrainLine> getTrainLineFromJson(){
        String json ="null";
        try{
            json= JsonUtils.readJsonData("src/main/resources/static/TrainLineList.json");
        }catch (IOException e){
            e.printStackTrace();
        }
        //将json数组转化为TrainLine对象集合
        List<TrainLine> listTrainLine = JSON.parseArray(json,TrainLine.class);
        return listTrainLine;
    }


}
