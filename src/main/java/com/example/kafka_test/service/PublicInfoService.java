package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dao.TrainFaultDao;
import com.example.kafka_test.dao.TrainInfoHvacDao;
import com.example.kafka_test.dto.MyResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class PublicInfoService {


    TrainCardDao trainCardDao;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    public Map<String, String> getPublicInfo(String lineNum, String trainNum) {
        String trainKey = getTrainKey(lineNum, trainNum);
        Map<String, String> res = new HashMap<>();
        if (trainCardDao.getResTrainCard().get(trainKey) == null) return res;

        Map<String, String> resTmp = processKafkaRecordUtils.removeKeySpace(trainCardDao.getResTrainCard().get(trainKey));

//        System.out.println(trainInfoHvacDao.getTrainInfoHvac());
//        System.out.println(trainInfoHvacDao.getTrainInfoHvac().get(trainKey).get(" trainspeed"));
        res.put("trainspeed", resTmp.get("trainspeed"));
        res.put("mainairpressure", resTmp.get("mainairpressure"));
        res.put("brakepressure", resTmp.get("brakepressure"));
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
