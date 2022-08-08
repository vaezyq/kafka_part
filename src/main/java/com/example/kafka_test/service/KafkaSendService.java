package com.example.kafka_test.service;

import com.example.kafka_test.dao.KafkaSendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Component
public class KafkaSendService {


    @Autowired
    KafkaSendDao kafkaSendDao;

    public final static Map<String, String> res_without_blank = new HashMap<>();


    //长度为25的数组，每个数组的时间间隔为5分钟以上


    public Map<String, String> getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

//        String trainKey = getTrainKey(lineNum, trainNum);

        String trainKey = "7002";

//        System.out.println(kafkaSendDao.getTrainInfoHvac());
        Map<String, String> res = new HashMap<>();
        if (kafkaSendDao.getTrainInfoHvac().get(trainKey) == null) {
            System.out.println("The specified train has no data yet");
            return res;
        } else {
            res = kafkaSendDao.processTrainCardHavc(kafkaSendDao.getTrainInfoHvac().get(trainKey));
        }


        Map<String, List<String>> resTempList = kafkaSendDao.getTemList(kafkaSendDao.getTrainInfoHvacList(), trainKey);

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

        for (int i = kafkaSendDao.getTrainInfoHvacListIdx() + 1; i < kafkaSendDao.getInsertListDate().size(); ++i) {
            temDate.add(sdf1.format(kafkaSendDao.getInsertListDate().get(i)));
        }
        for (int i = 0; i <= kafkaSendDao.getTrainInfoHvacListIdx(); ++i) {
            temDate.add(sdf1.format(kafkaSendDao.getInsertListDate().get(i)));
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


    //得到每个卡片的具体字段数目
    public int countNum(String str) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf('=', index)) != -1) {
            count++;
            index += 1;
        }
        return count;
    }


    public Map<String, String> getTrainDoor(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
        Map<String, String> doorMap = kafkaSendDao.processTrainCardHavc(kafkaSendDao.getTrainInfoDoor().get(trainKey));

        for (Map.Entry<String, String> entry : doorMap.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {
                res_without_blank.put(entry.getKey().substring(1, entry.getKey().length()), entry.getValue().toString());
            } else {
                res_without_blank.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return res_without_blank;
    }

    public Map<String, String> getTrainPis(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
        Map<String, String> pisMap = kafkaSendDao.processTrainCardHavc(kafkaSendDao.getTrainInfoPis().get(trainKey));
        return removeKeySpace(pisMap);
    }

    public Map<String, String> getTrainBaseInfo(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        String trainKey = "7002";
        System.out.println(kafkaSendDao.getTrainInfoBase());
        Map<String, String> trainInfoMap = kafkaSendDao.getTrainInfoBase().get(trainKey);
        return removeKeySpace(trainInfoMap);
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

}
