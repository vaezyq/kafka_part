package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dao.TrainInfoHvacDao;
import com.example.kafka_test.dto.AirCondResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TrainInfoHvacService {

    @Autowired
    TrainInfoHvacDao trainInfoHvacDao;


    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    @Autowired
    TrainCardDao trainCardDao;

    public final static Map<String, String> res_without_blank = new HashMap<>();

    private List<String> carriageName = new ArrayList() {{
        add("tc1");
        add("tc2");
        add("m1");
        add("m2");
        add("mp1");
        add("mp2");
    }};

    public Map<String, String> getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) throws ParseException {

        String trainKey = getTrainKey(lineNum, trainNum);
        System.out.println(trainKey);

//        String trainKey = "7002";

//        System.out.println(kafkaSendDao.getTrainInfoHvac());
        Map<String, String> res = new HashMap<>();
        if (trainInfoHvacDao.getTrainInfoHvac() == null) {
            System.out.println("The specified train has no data yet");
            return res;
        } else {
            res = trainInfoHvacDao.getTrainInfoHvac().get(trainKey);
        }


        Map<String, List<String>> resTempList = trainInfoHvacDao.getTemList(trainInfoHvacDao.getTrainInfoHvacList(), trainKey);

        Map<String, String> resTemp = new HashMap<>();

//        Map<String, String> res_without_blank = new HashMap<>();

        for (Map.Entry<String, String> entry : res.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {
                res_without_blank.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                res_without_blank.put(entry.getKey(), entry.getValue().toString());
            }
        }

        for (Map.Entry<String, List<String>> entry : resTempList.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {
                resTemp.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                resTemp.put(entry.getKey(), entry.getValue().toString());
            }
        }


        //resTemp字段中数组的顺序需要调整
        for (Map.Entry<String, String> entry : resTemp.entrySet()) {
            res_without_blank.replace(entry.getKey(), entry.getValue());
        }

//        // 添加日期字段，一共25个
//        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//
//        ArrayList<String> temDate = new ArrayList<>();
//
//        for (int i = trainInfoHvacDao.getTrainInfoHvacListIdx() + 1; i < trainInfoHvacDao.getInsertListDate().size(); ++i) {
//            temDate.add(sdf1.format(trainInfoHvacDao.getInsertListDate().get(i)));
//        }
//        for (int i = 0; i <=trainInfoHvacDao.getTrainInfoHvacListIdx(); ++i) {
//            temDate.add(sdf1.format(trainInfoHvacDao.getInsertListDate().get(i)));
//        }
//        res_without_blank.put("date", temDate.toString());
//        System.out.println(res_without_blank.get("date"));

        return res_without_blank;
    }


    public String getTrainKey(String lineNum, String trainNum) {
        String trainKey = "";
        if (lineNum.length() == 1) {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "00" + trainNum;
            } else {
                trainKey = lineNum + "0" + trainNum;
            }
        } else {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "0" + trainNum;
            } else {
                trainKey = lineNum + trainNum;
            }
        }
        return trainKey;
    }


    ArrayList<ArrayList<HashMap<String, String>>> airCondEditionInfo = new ArrayList<>();


    //  处理空调返回数据接口的model
    public ArrayList<ArrayList<HashMap<String, String>>> processModel(Map<String, String> specificTrainKeyMap) {


        Map<String, String> trainInfoTemp = removeKeySpace(specificTrainKeyMap);
        //目前车厢的数目默认最大是6，然后每个车厢的空调的数目默认最大也是6
        int carriageNum = 6;
        int airNum = 2;
        ArrayList<ArrayList<HashMap<String, String>>> airModel = new ArrayList<>();

        //查询的字段模式 tc1hvac1mode

        for (int i = 0; i < carriageName.size(); ++i) {
            ArrayList<HashMap<String, String>> carriageTemp = new ArrayList<>();
            for (int j = 1; j <= airNum; ++j) {
                String airCondKey = carriageName.get(i) + "hvac" + j + "mode";
                if (trainInfoTemp.containsKey(airCondKey)) {
                    HashMap<String, String> airState = new HashMap<>();
                    airState.put("airName", "" + (i + 1));
                    airState.put("airPattern", trainInfoTemp.get(airCondKey));
                    // 这里代码可能有问题
                    carriageTemp.add((HashMap<String, String>) airState.clone());

                }
            }
            airModel.add(carriageTemp);
        }
        return airModel;
    }

    //  处理空调返回数据接口的edition
    public ArrayList<ArrayList<HashMap<String, String>>> processEdition(Map<String, String> specificTrainKeyMap) {
        Map<String, String> trainInfoTemp = removeKeySpace(specificTrainKeyMap);
        //目前车厢的数目默认最大是6，然后每个车厢的空调的数目默认最大也是6
        int carriageNum = 6;
        int airNum = 6;

        ArrayList<ArrayList<HashMap<String, String>>> airEdition = new ArrayList<>();

        //查询的字段模式 Tc1Hvac1SoftVersion
        for (int i = 0; i < carriageName.size(); ++i) {
            ArrayList<HashMap<String, String>> carriageTemp = new ArrayList<>();
            for (int j = 1; j <= airNum; ++j) {
                String airCondKey = carriageName.get(i) + "hvac" + j + "softversion";
                if (trainInfoTemp.containsKey(airCondKey)) {
                    HashMap<String, String> airState = new HashMap<>();
                    airState.put("airName", "" + (i + 1));
                    airState.put("airPattern", trainInfoTemp.get(airCondKey));
                    // 这里代码可能有问题
                    carriageTemp.add((HashMap<String, String>) airState.clone());

                }
            }
            airEdition.add(carriageTemp);

        }
        return airEdition;
    }

    public Map<String, String> removeKeySpace(Map<String, String> trainInfo) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry : trainInfo.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {  //空格都是开头第一个
                res.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                res.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return res;
    }


    public Map<String, String> getTrainCarriageInfo(Map<String, String> trainInfoHvac) {
        Map<String, String> res = new LinkedHashMap<>();
        ArrayList<String> frontAndRearCarriageName = new ArrayList<>();
        frontAndRearCarriageName.add("tc1");
        frontAndRearCarriageName.add("tc2");
        ArrayList<String> middlePartCarriageName = new ArrayList<>();
        middlePartCarriageName.add("m1");
        middlePartCarriageName.add("m2");
        middlePartCarriageName.add("mp1");
        middlePartCarriageName.add("mp2");
        //添加车头和车尾的信息
        for (int i = 0; i < frontAndRearCarriageName.size(); ++i) {
            res.put("name" + frontAndRearCarriageName.get(i), frontAndRearCarriageName.get(i));
            res.put("temperature" + frontAndRearCarriageName.get(i), trainInfoHvac.get(frontAndRearCarriageName.get(i) + "temperature"));
            res.put("status" + frontAndRearCarriageName.get(i), "正常");
        }
        ArrayList<String> middlePartCarriageTemp = new ArrayList<>();

        //添加车的中间部分信息
//        for (int i = 0; i < middlePartCarriageName.size(); ++i) {
//            res_temp.put("name" + middlePartCarriageName.get(i), middlePartCarriageName.get(i));
//            res_temp.put("temperature" + middlePartCarriageName.get(i), trainInfoHvac.get(middlePartCarriageName.get(i) + "temperature"));
//            res_temp.put("statusType" + middlePartCarriageName.get(i), "正常");
//        }
        for (int i = 0; i < middlePartCarriageName.size(); ++i) {
            Map<String, String> res_temp = new LinkedHashMap<>();
            res_temp.put("name", middlePartCarriageName.get(i));
            res_temp.put("temperature", trainInfoHvac.get(middlePartCarriageName.get(i) + "temperature"));
            res_temp.put("statusType", "正常");
            middlePartCarriageTemp.add(res_temp.toString());
        }
        res.put("carriageList", middlePartCarriageTemp.toString());
        return res;
    }

    // tc1Hvac2ReturnDamperState：'',  //回风阀:两个状态   开：open  关：closed
    // tc1Hvac2IDamperState:'',      //新风阀:两个状态   开：open  关：closed
    // tc1Hvac2Mode:'',                 //空调模式
    // tc1Hvac2IExtTemp:'',          //室外温度
    // tc1Hvac2ITargetTemp:'',       //目标温度
    //
    // tc1Hvac2Compressor1State:'', //压缩机1状态
    // tc1Hvac2Compressor2State:'',  //压缩机2状态
    // tc1Hvac2Ventilation1State:'',  //通风机1状态
    // tc1Hvac2Ventilation2State:'',  //通风机2状态
    // tc1Hvac2Heater1State:'',       //电加热器1状态
    // tc1Hvac2Heater2State:'',       //电加热器2状态


    public Map<String, String> getOneDimAirCondInfo(Map<String, String> trainInfoHvac) {
        Map<String, String> res = new LinkedHashMap<>();
        String key = "";

        for (int i = 0; i < carriageName.size(); ++i) {
            for (int j = 1; j <= 2; ++j) {
                key = carriageName.get(i) + "hvac" + j + "returndamperstate";
                res.put(key, trainInfoHvac.get(key));
                key = carriageName.get(i) + "hvac" + j + "idamperstate";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Mode:'',                 //空调模式
                key = carriageName.get(i) + "hvac" + j + "mode";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2IExtTemp:'',          //室外温度
                key = carriageName.get(i) + "hvac" + j + "iexttemp";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2ITargetTemp:'',       //目标温度
                key = carriageName.get(i) + "hvac" + j + "itargettemp";
                res.put(key, trainInfoHvac.get(key));
                //
                // tc1Hvac2Compressor1State:'', //压缩机1状态
                key = carriageName.get(i) + "hvac" + j + "compressor1state";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Compressor2State:'',  //压缩机2状态
                key = carriageName.get(i) + "hvac" + j + "compressor2state";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Ventilation1State:'',  //通风机1状态
                key = carriageName.get(i) + "hvac" + j + "ventilation1state";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Ventilation2State:'',  //通风机2状态
                key = carriageName.get(i) + "hvac" + j + "ventilation2state";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Heater1State:'',       //电加热器1状态
                key = carriageName.get(i) + "hvac" + j + "heater1state";
                res.put(key, trainInfoHvac.get(key));
                // tc1Hvac2Heater2State:'',       //电加热器2状态
                key = carriageName.get(i) + "hvac" + j + "heater1state";
                res.put(key, trainInfoHvac.get(key));
            }
        }
        return res;
    }


    public Map<String, String> getAirCondResult(String lineNum, String trainNum) throws ParseException {


        Map<String, String> baseInfo = new LinkedHashMap<>();

        Map<String, String> trainCardRes = new LinkedHashMap<>();

        String trainKey = getTrainKey(lineNum, trainNum);
//        System.out.println(trainKey);

        if (trainCardDao.getResTrainCard() == null) {
            System.out.println("The specified train has no card data yet");
            return baseInfo;
        } else {
            if (trainCardDao.getResTrainCard().containsKey(trainKey)) {
                trainCardRes = removeKeySpace(trainCardDao.getResTrainCard().get(trainKey));
            } else {
                System.out.println("The specified train has no card data yet");
                return baseInfo;
            }
            //拿到指定列车号的数据
        }
        baseInfo.put("carSpeed", trainCardRes.get("trainspeed"));
        baseInfo.put("airPressure", trainCardRes.get("mainairpressure"));
        baseInfo.put("breakPressure", trainCardRes.get("brakepressure"));


        Map<String, String> airResponse = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : baseInfo.entrySet()) {
            airResponse.put(entry.getKey(), entry.getValue());
        }


        AirCondResponse airCondResponse = new AirCondResponse();
        Map<String, String> res = new HashMap<>();
        if (trainInfoHvacDao.getTrainInfoHvac() == null) {
            System.out.println("The specified train has no data yet");
            return airResponse;
        } else {
            if (trainInfoHvacDao.getTrainInfoHvac().containsKey(trainKey)) {
                res = removeKeySpace(trainInfoHvacDao.getTrainInfoHvac().get(trainKey));
            } else {
                System.out.println("The specified train has no data yet");
            }
            //拿到指定列车号的数据
        }

        airCondResponse.setAirModel(processModel(res));
        airCondResponse.setAirEdition(processEdition(res));
        airCondResponse.setTrainTemAndStatus(getTrainCarriageInfo(res));
        airCondResponse.setTempList(trainInfoHvacDao.getTemList(trainInfoHvacDao.getTrainInfoHvacList(), trainKey));

        //剩余一维信息\
        airCondResponse.setOneDimAirCondInfo(getOneDimAirCondInfo(res));
        // 车厢部分数据，不包含车头和车尾
        airResponse.put("airPatternList", airCondResponse.getAirModel().toString());
        airResponse.put("airEditionList", airCondResponse.getAirEdition().toString());

        for (Map.Entry<String, String> entry : airCondResponse.getTrainTemAndStatus().entrySet()) {
            airResponse.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, List<String>> entry : airCondResponse.getTempList().entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {  //空格都是开头第一个
                airResponse.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                airResponse.put(entry.getKey(), entry.getValue().toString());
            }
        }

        for (Map.Entry<String, String> entry : airCondResponse.getOneDimAirCondInfo().entrySet()) {
            airResponse.put(entry.getKey(), entry.getValue());
        }

//        baseInfo.put("airPatternList", airPatternListResponse.toString());


        return airResponse;
    }


}



















