package com.example.kafka_test.service;

import com.example.kafka_test.dao.KafkaSendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

@Service
@Component
public class KafkaSendService {


    @Autowired
    KafkaSendDao kafkaSendDao;

    public final  static Map<String, String> res_without_blank = new HashMap<>();


    //长度为25的数组，每个数组的时间间隔为5分钟以上


    public Map<String, String> getTrainHvac(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {

//        String trainKey = getTrainKey(lineNum, trainNum);

        String trainKey = "7002";

        System.out.println(kafkaSendDao.getTrainInfoHvac());
        Map<String, String> res = kafkaSendDao.processTrainCardHavc(kafkaSendDao.getTrainInfoHvac().get(trainKey));

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
        res_without_blank.put("date", kafkaSendDao.getInsertListDate().toString());
        System.out.println(res_without_blank.get("date"));

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


}
