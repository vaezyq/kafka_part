package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TrainInfoHvacDao {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    //车辆hvac
    private static final HashMap<String, String> trainInfoHvac = new HashMap<>();

    //上一次插入的位置
    private static int trainInfoHvacListIdx = 0;

    public static int getTrainInfoHvacListIdx() {
        return trainInfoHvacListIdx;
    }

    private static final String train_info_hvac = "traininfo_hvac";


    public HashMap<String, String> getTrainInfoHvac() {
//        System.out.println(trainInfoHvac);
        return trainInfoHvac;
    }

    private List<HashMap<String, String>> trainInfoHvacList = new ArrayList<HashMap<String, String>>() {{
        for (int i = 0; i < 25; ++i) {
            HashMap<String, String> hashMap = new HashMap<>();
            add(hashMap);
        }
    }};

    //每一次插入的时间
    private List<Date> insertListDate = new ArrayList<Date>() {{
        for (int i = 0; i < 25; ++i) {
            add(new Date());
        }
    }};

    public List<Date> getInsertListDate() {
        return insertListDate;
    }


    public List<HashMap<String, String>> getTrainInfoHvacList() {
        return trainInfoHvacList;
    }


    // train_info_hvac页面
    @KafkaListener(id = "", topics = train_info_hvac, groupId = "group.train_hvac")
    public void listenerTrainInfoHvac(ConsumerRecord<?, ?> record) {
        if (trainInfoHvac.containsKey("" + record.key())) {
            trainInfoHvac.replace("" + record.key(), "" + record.value());
//            Map<String, String> processResMap = processTrainCardHavc(record.value().toString());
//            Map<String, String> resMap = new HashMap<>();
//            for (String key : processResMap.keySet()) {
//                if (key.indexOf(" ") == 0) {
//                    resMap.put(key.substring(1, key.length() - 1), processResMap.get(key));
//                }
//            }
//            Object obj = JSONArray.toJSON(resMap);
        } else {
//            Object obj = JSONArray.toJSON(processTrainCardHavc(record.value().toString()));
            trainInfoHvac.put("" + record.key(), "" + record.value());
        }

        //每隔5min以上才会进行一次插入，后续再用，目前直接插入不进行判定

        //最开始这个进行插入(否则无法遍历map表)

        boolean flag = true;  //判断
        for (int i = 0; i < trainInfoHvacList.size(); ++i) {
            if (trainInfoHvacList.get(i).size() != 0) {
                flag = false;
            }
        }
        Date date = new Date();

        if (flag) {  //没有数据时插入一条
            trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
            insertListDate.set(trainInfoHvacListIdx, date);
        }

        if (date.getMinutes() - insertListDate.get(trainInfoHvacListIdx).getMinutes() >= 1) {
            trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //本次更新的位置
            trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
            insertListDate.set(trainInfoHvacListIdx, date);

        } else if (date.getMinutes() - insertListDate.get(trainInfoHvacListIdx).getMinutes() < 0) {
            if ((date.getMinutes() + (60 - insertListDate.get(trainInfoHvacListIdx).getMinutes())) >= 1) {
                trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //本次更新的位置
                trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
                insertListDate.set(trainInfoHvacListIdx, date);
            }
        }

//        trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
//        trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //下一次更新的位置
//        insertListDate.set(trainInfoHvacListIdx, date);
    }


    public Map<String, List<String>> getTemList(List<HashMap<String, String>> trainCardHvacList, String trainKey) { //25个数据，一个数据间隔5分钟
//        System.out.println(trainInfoHvacListIdx);
        String temList[] = {"returndampertemp", "senddampertemp", "idampertemp", "cooltemp", "inhaletemp", "exhausttemp", "outevaporationtemp", "evaporationtemp", "targettemp"};
        Map<String, List<String>> temResList = new HashMap<>();
//        System.out.println(trainCardHvacList.get(0));
        if (trainCardHvacList.get(0).containsKey(trainKey)) {    //如果包含这辆列车
            Map<String, String> trainKeyCardMap = processKafkaRecordUtils.processTrainRecord(trainCardHvacList.get(0).get(trainKey).toString());
            for (Map.Entry<String, String> entry : trainKeyCardMap.entrySet()) {
//                System.out.println(entry.getKey().toString());
                for (int i = 0; i < temList.length; ++i) {
                    if (entry.getKey().toString().contains(temList[i])) {     //需要将这个字段变为数组
                        List<String> temp = new ArrayList<>();
                        for (int j = trainInfoHvacListIdx + 1; j < trainCardHvacList.size(); ++j) {
                            if (trainCardHvacList.get(j).size() == 0) {
                                temp.add("0");
                            } else {
                                Map<String, String> trainCardMap = processKafkaRecordUtils.processTrainRecord(trainCardHvacList.get(j).get(trainKey).toString());
                                temp.add(trainKeyCardMap.get(entry.getKey()));
                            }
                        }
                        for (int j = 0; j <= trainInfoHvacListIdx; ++j) {
                            if (trainCardHvacList.get(j).size() == 0) {
                                temp.add("0");
                            } else {
                                Map<String, String> trainCardMap = processKafkaRecordUtils.processTrainRecord(trainCardHvacList.get(j).get(trainKey).toString());
                                temp.add(trainKeyCardMap.get(entry.getKey()));
                            }
                        }
                        temResList.put(entry.getKey(), temp);
                    }
                }
            }
        }
        return temResList;
    }


}
