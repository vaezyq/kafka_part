package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainInfoHvacDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TrainInfoHvacService {

    @Autowired
    TrainInfoHvacDao trainInfoHvacDao;


    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    public final static Map<String, String> res_without_blank = new HashMap<>();

    public Map<String, String> getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

//        String trainKey = getTrainKey(lineNum, trainNum);

        String trainKey = "7002";

//        System.out.println(kafkaSendDao.getTrainInfoHvac());
        Map<String, String> res = new HashMap<>();
        if (trainInfoHvacDao.getTrainInfoHvac().get(trainKey) == null) {
            System.out.println("The specified train has no data yet");
            return res;
        } else {
            res = processKafkaRecordUtils.processTrainRecord(trainInfoHvacDao.getTrainInfoHvac().get(trainKey));
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

        // 添加日期字段，一共25个
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        ArrayList<String> temDate = new ArrayList<>();

        for (int i = trainInfoHvacDao.getTrainInfoHvacListIdx() + 1; i < trainInfoHvacDao.getInsertListDate().size(); ++i) {
            temDate.add(sdf1.format(trainInfoHvacDao.getInsertListDate().get(i)));
        }
        for (int i = 0; i <=trainInfoHvacDao.getTrainInfoHvacListIdx(); ++i) {
            temDate.add(sdf1.format(trainInfoHvacDao.getInsertListDate().get(i)));
        }
        res_without_blank.put("date", temDate.toString());
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

}
