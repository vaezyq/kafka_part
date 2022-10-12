package com.example.kafka_test.utils;

import com.alibaba.fastjson.JSON;
import com.example.kafka_test.dao.ProcessLineDataUtils;
import com.example.kafka_test.dto.Distance;
import com.example.kafka_test.dto.StationLocation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class TrainLinesUtils {

    private HashMap<String, StationLocation> stringStationLocationHashMap = new HashMap<String, StationLocation>();

    private HashMap<Distance, Double> distanceDoubleHashMap = new HashMap<Distance, Double>();


    //无参构造方法，初始化stringStationLocationHashMap和distanceDoubleHashMap;
    public TrainLinesUtils() {
        String stationLocationJson = "null";
        String distanceJson = "null";
        ProcessLineDataUtils pldu = new ProcessLineDataUtils();
        try {
//            stationLocationJson = JsonUtils.readJsonData("src/main/resources/static/stationLocation.json");
//            distanceJson = JsonUtils.readJsonData("src/main/resources/static/distance.json");
//            stationLocationJson = JsonUtils.readJsonData("./stationLocation.json");
//            distanceJson = JsonUtils.readJsonData("./distance.json");
            stationLocationJson = pldu.getTrainLineJson("stationLocation");
            distanceJson = pldu.getTrainLineJson("distance");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //将json数组转化为TrainStation对象集合
        List<StationLocation> stationLocations = JSON.parseArray(stationLocationJson, StationLocation.class);
        List<Distance> distances = JSON.parseArray(distanceJson, Distance.class);
        for (StationLocation sl : stationLocations) {
            stringStationLocationHashMap.put(sl.getId(), sl);
        }
        for (Distance distance : distances) {
            distanceDoubleHashMap.put(distance, distance.getDistance());
        }
    }

    public double[] queryPositionById(String id) {
        if (!stringStationLocationHashMap.containsKey(id)) {
            //System.out.println("找不到这个车站的位置");
            return null;
        } else return stringStationLocationHashMap.get(id).getValue();
    }

    public String queryStationNameById(String id){
        if (!stringStationLocationHashMap.containsKey(id)) {
            //System.out.println("找不到这个车站的名称");
            return null;
        } else return stringStationLocationHashMap.get(id).getName();
    }

    public double queryDistanceById(String id1, String id2) {
        Distance d1 = new Distance(id1, id2);
        Distance d2 = new Distance(id2, id1);
        if (distanceDoubleHashMap.containsKey(d1)) return distanceDoubleHashMap.get(d1);
            //考虑反方向列车的情况，距离只需要存一份
        else if (distanceDoubleHashMap.containsKey(d2)) return distanceDoubleHashMap.get(d2);
        else return -1;
    }

    /**
     * 给出当前站和下一站，以及运行的进度，算出车辆当前的坐标
     *
     * @param source
     * @param target
     * @param rate
     * @return
     */
    public double[] calculateCoordinate(String source, String target, double rate) {
        double[] sourceCoordinate = new double[2];
        sourceCoordinate[0] = queryPositionById(source)[0];
        sourceCoordinate[1] = queryPositionById(source)[1];
        double[] targetCoordinate = new double[2];
        targetCoordinate[0] = queryPositionById(target)[0];
        targetCoordinate[1] = queryPositionById(target)[1];
        double[] coordinate = new double[2];
        coordinate[0] = sourceCoordinate[0] + (targetCoordinate[0] - sourceCoordinate[0]) * rate;
        coordinate[1] = sourceCoordinate[1] + (targetCoordinate[1] - sourceCoordinate[1]) * rate;
        return coordinate;
    }

    /**
     * 计算以source为原点，source-target连成的射线与x轴正向逆时针旋转的夹角（范围：0-359.999）
     *
     * @param source
     * @param target
     * @return
     */
    public double calculateDirection(String source, String target) {
        double[] sourceCoordinate = new double[]{queryPositionById(source)[0], queryPositionById(source)[1]};
        double[] targetCoordinate = new double[]{queryPositionById(target)[0], queryPositionById(target)[1]};
        double angle = Math.atan2(targetCoordinate[1] - sourceCoordinate[1], targetCoordinate[0] - sourceCoordinate[0]);
        double theta = angle * (180 / Math.PI);
        if (targetCoordinate[1] >= sourceCoordinate[1]) return theta;
        else return 360.0 + theta;
    }


}
