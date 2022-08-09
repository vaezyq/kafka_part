package com.example.kafka_test.dao;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessKafkaRecordUtils {

    public Map<String, String> processRecordAndString(String key, String record) {
        // 7002_2022-07-06 18:45:29 159
        String resDate = key.substring(5, 24);
        Map<String, String> resMap = processTrainRecord(record);
        resMap.put("date", resDate);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resMap.put("updateDate", sdf.format(date));
        return resMap;
    }

    public static Map<String, String> processTrainRecord(String str) {
        Map<String, String> trainCardHavc = new HashMap<>();
        int comma_idx_fir = 0;
        int equal_idx = 0;
        int comma_idx_sec = -1;
//        System.out.println(str);
        while ((comma_idx_sec = str.indexOf(',', comma_idx_sec + 1)) != -1) {
            equal_idx = str.indexOf('=', equal_idx + 1);
            String key = str.substring(comma_idx_fir + 1, equal_idx);
            String value = str.substring(equal_idx + 1, comma_idx_sec);
            trainCardHavc.put(key, value);
            comma_idx_fir = comma_idx_sec;
        }
        equal_idx = str.indexOf('=', equal_idx + 1);
        trainCardHavc.put(str.substring(comma_idx_fir + 1, equal_idx), str.substring(equal_idx + 1, str.length() - 1));
        return trainCardHavc;
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
