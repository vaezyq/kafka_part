package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class TrainInfoHvacDao {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    //车辆hvac
    private static final HashMap<String, Map<String, String>> trainInfoHvac = new HashMap<>();

    //上一次插入的位置
    private static int trainInfoHvacListIdx = 0;

    public static int getTrainInfoHvacListIdx() {
        return trainInfoHvacListIdx;
    }

    private static final String train_info_hvac = "traininfo_hvac";

    @Value("${diff}")
    private int diff = 5;


    public HashMap<String, Map<String, String>> getTrainInfoHvac() {
//        System.out.println(trainInfoHvac);
        return trainInfoHvac;
    }

    //因为目前一秒插入两个，然后显示时一秒两个，所以间隔
    private List<HashMap<String, Map<String, String>>> trainInfoHvacList = new ArrayList<HashMap<String, Map<String, String>>>() {{
        for (int i = 0; i < 50; ++i) {
            HashMap<String, Map<String, String>> hashMap = new HashMap<>();
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


    public List<HashMap<String, Map<String, String>>> getTrainInfoHvacList() {
        return trainInfoHvacList;
    }


    // train_info_hvac页面
    @KafkaListener(id = "", topics = train_info_hvac, groupId = "new_12")
    public void listenerTrainInfoHvac(ConsumerRecord<?, ?> record) {

        if (trainInfoHvac.containsKey(record.key().toString().substring(0, 4))) {
            trainInfoHvac.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
        } else {
            trainInfoHvac.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
        }
        System.out.println(trainInfoHvac.get(record.key().toString().substring(0, 4)).get("date"));
        System.out.println(record.key());
        HashMap<String, Map<String, String>> res = new HashMap<>(trainInfoHvac);

        trainInfoHvacList.set(trainInfoHvacListIdx, res);
        trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 50;
        System.out.println(trainInfoHvacListIdx);
//        System.out.println(record.key());

    }


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
//            for (int i = 0; i < trainInfoHvacListIdx; ++i) {
//                System.out.println(trainCardHvacList.get(i).get(trainKey).get("date"));
//            }


            temResList.put("date", date);
        }


        return temResList;
    }


}
