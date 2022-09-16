package com.example.kafka_test.utils;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.kafka_test.dao.ProcessKafkaRecordUtils.processTrainRecord;

public class ListenerTrainInfoHvacThread extends Thread {


    @Autowired
    KafkaProperties kafkaProperties;


    // 空调部分需要使用的数据字段
    public static final String train_info_hvac = "traininfo_hvac";
    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;
    public static final HashMap<String, Map<String, String>> trainInfoHvac = new HashMap<>();
    //上一次插入的位置
    private static int trainInfoHvacListIdx = 0;

    public static int getTrainInfoHvacListIdx() {
        return trainInfoHvacListIdx;
    }

    // 后续从properties中取这个配置数据
    @Value("${diff}")
    private int diff = 20;

    //因为目前一秒插入两个，然后显示时一秒两个，所以间隔
    private List<HashMap<String, Map<String, String>>> trainInfoHvacList = new ArrayList<HashMap<String, Map<String, String>>>() {{
        for (int i = 0; i < 50; ++i) {
            HashMap<String, Map<String, String>> hashMap = new HashMap<>();
            add(hashMap);
        }
    }};

    public List<HashMap<String, Map<String, String>>> getTrainInfoHvacList() {
        return trainInfoHvacList;
    }

    public HashMap<String, Map<String, String>> getTrainInfoHvac() {
        return trainInfoHvac;
    }


    // 重写thread中的run函数，同时所有topic使用一个消费者组
    @Override
    public void run() {


        //空调部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_hvac = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_hvac.subscribe(Arrays.asList(train_info_hvac));

        while (true) {
            //空调部分的数据处理
            ConsumerRecords<String, String> records_hvac = kafkaConsumer_hvac.poll(500);
            for (ConsumerRecord<String, String> record : records_hvac) {
                if (trainInfoHvac.containsKey(record.key().toString().substring(0, 4))) {
                    trainInfoHvac.replace(record.key().toString().substring(0, 4), processRecordAndString(record.key().toString(), record.value().toString()));
                } else {
                    trainInfoHvac.put(record.key().toString().substring(0, 4), processRecordAndString(record.key().toString(), record.value().toString()));
                }
//                    System.out.println(record.key());
                HashMap<String, Map<String, String>> res = new HashMap<>(trainInfoHvac);

                trainInfoHvacList.set(trainInfoHvacListIdx, res);
                trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 50;
//                System.out.println(record.value());
            }
//            if (trainInfoHvac.containsKey("7002")) {
//                System.out.println(getAllTrainTemAndStatus(removeKeySpace(trainInfoHvac.get("7002"))));
//            }


//            System.out.println(trainInfoHvac);
        }
    }

    // 处理kafka接收到的数据结果:  key  value
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


    // 得到空调部分，需要存储历史信息的部分字段的列表结果
    public Map<String, List<String>> getTemList(List<HashMap<String, Map<String, String>>> trainCardHvacList, String trainKey) throws ParseException { //25个数据，一个数据间隔5分钟
        String temList[] = {"returndampertemp", "senddampertemp", "idampertemp", "cooltemp", "inhaletemp", "exhausttemp", "outevaporationtemp", "evaporationtemp", "targettemp"};
        Map<String, List<String>> temResList = new HashMap<>();
        Date nowDate = new Date();
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(diff);
        if (trainCardHvacList.get(0).containsKey(trainKey)) {    //如果包含这辆列车
            Map<String, String> trainKeyCardMap = trainCardHvacList.get(0).get(trainKey);
            for (Map.Entry<String, String> entry : trainKeyCardMap.entrySet()) {
//                System.out.println(entry.getKey().toString());
                for (int i = 0; i < temList.length; ++i) {
                    if (entry.getKey().toString().contains(temList[i])) {     //需要将这个字段变为数组
                        List<String> temp = new ArrayList<>();
                        for (int j = trainInfoHvacListIdx + 1; j < trainCardHvacList.size(); j += 2) {
                            if (trainCardHvacList.get(j).size() == 0) {
                                temp.add("0");
                            } else {

                                int mindiff = nowDate.getMinutes() - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                                if (mindiff >= 0) {
                                    if (mindiff > diff) {
                                        continue;
                                    }
                                } else {
                                    mindiff = nowDate.getMinutes() + 60 - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                                    if (mindiff > diff) {
                                        continue;
                                    }
                                }

                                Map<String, String> trainCardMap = trainCardHvacList.get(j).get(trainKey);
                                temp.add(trainKeyCardMap.get(entry.getKey()));
                            }
                        }
                        for (int j = (trainInfoHvacListIdx - 1) % 2; j < trainInfoHvacListIdx; j += 2) {
                            if (trainCardHvacList.get(j).size() == 0) {
                                temp.add("0");
                            } else {
                                int mindiff = nowDate.getMinutes() - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                                if (mindiff >= 0) {
                                    if (mindiff > diff) {
                                        continue;
                                    }
                                } else {
                                    mindiff = nowDate.getMinutes() + 60 - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                                    if (mindiff > diff) {
                                        continue;
                                    }
                                }
                                Map<String, String> trainCardMap = trainCardHvacList.get(j).get(trainKey);
                                temp.add(trainKeyCardMap.get(entry.getKey()));

                            }
                        }
                        temResList.put(entry.getKey(), temp);
                    }
                }
            }

            List<String> date = new ArrayList<>();
            for (int j = trainInfoHvacListIdx + 1; j < trainCardHvacList.size(); j += 2) {
                if (trainCardHvacList.get(j).size() == 0) {
                    date.add(sdf1.format(new Date()));
                } else {
                    int mindiff = nowDate.getMinutes() - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                    if (mindiff >= 0) {
                        if (mindiff > diff) {
                            continue;
                        }
                    } else {
                        mindiff = nowDate.getMinutes() + 60 - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                        if (mindiff > diff) {
                            continue;
                        }
                    }
                    date.add(trainCardHvacList.get(j).get(trainKey).get("date"));
                }
            }
            for (int j = (trainInfoHvacListIdx - 1) % 2; j < trainInfoHvacListIdx; j += 2) {
                if (trainCardHvacList.get(j).size() == 0) {
                    date.add(sdf1.format(new Date()));
                } else {
                    int mindiff = nowDate.getMinutes() - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                    if (mindiff >= 0) {
                        if (mindiff > diff) {
                            continue;
                        }
                    } else {
                        mindiff = nowDate.getMinutes() + 60 - sdf1.parse(trainCardHvacList.get(j).get(trainKey).get("date")).getMinutes();
                        if (mindiff > diff) {
                            continue;
                        }
                    }
                    date.add(trainCardHvacList.get(j).get(trainKey).get("date"));
                }
            }
            temResList.put("date", date);
        }
        return temResList;
    }


    public Map<String, String> getAllTrainTemAndStatus(Map<String, String> trainInfo) {
        ArrayList<String> trainName = new ArrayList<>();
        HashMap<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry : trainInfo.entrySet()) {
            if (entry.getKey().contains("temperature")) {
                trainName.add(entry.getKey().substring(0, entry.getKey().indexOf("temperature")));
            }
        }
        for (int i = 0; i < trainName.size(); ++i) {
            String trainKey = "name" + trainName.get(i);
            res.put(trainKey, trainName.get(i));
            res.put("temperature" + trainName.get(i), trainInfo.get(trainName.get(i) + "temperature"));
            res.put("state" + trainName.get(i), "正常");
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

}