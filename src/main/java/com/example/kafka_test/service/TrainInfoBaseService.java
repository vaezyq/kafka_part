package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dao.TrainInfoBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TrainInfoBaseService {

    @Autowired
    TrainInfoBaseDao trainInfoBaseDao;

    @Autowired
    TrainInfoHvacService trainInfoHvacService;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    @Autowired
    TrainCardDao trainCardDao;


    public Map<String, Object> getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
//        String trainKey = "7002";
        Map<String, Object> res = new LinkedHashMap<>();
        String trainKey = getTrainKey(lineNum, trainNum);

        Map<String, String> trainCardRes = new LinkedHashMap<>();


        if (trainCardDao.getResTrainCard() == null) {
            System.out.println("The specified train has no card data yet");
            return res;
        } else {
            if (trainCardDao.getResTrainCard().containsKey(trainKey)) {
                trainCardRes = removeKeySpace(trainCardDao.getResTrainCard().get(trainKey));
            } else {
                System.out.println("The specified train has no card data yet");
                return res;
            }
            //拿到指定列车号的数据
        }
        if (trainCardRes.containsKey("trainspeed")) {
            res.put("carSpeed", String.format("%.2f", Float.parseFloat(trainCardRes.get("trainspeed")) / 10));
        }
        res.put("airPressure", trainCardRes.get("mainairpressure"));
        res.put("brakePressure", trainCardRes.get("brakepressure"));

        if (trainKey.length() != 4) {
            return new HashMap<>();
        }

        Map<String, String> trainInfoMap = trainInfoBaseDao.getTrainInfoBase().get(trainKey);
        if (trainInfoMap == null) return res;

        Map<String, String> resTemp = processKafkaRecordUtils.removeKeySpace(trainInfoMap);

        for (Map.Entry<String, String> entry : resTemp.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
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


}
