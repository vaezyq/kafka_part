package com.example.kafka_test.dao;


import com.alibaba.fastjson.JSONArray;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


//因为这里数据从kafka取出，所以这里实现kafka监听器部分，不需要实现数据库部分


@Component
public class KafkaSendDao {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //  ddu信息
    private static final HashMap<String, String> resDdu = new HashMap<>();




    // 车辆卡片
    private static final HashMap<String, String> resTrainCard = new HashMap<>();

    // 车辆故障
    private static final HashMap<String, String> resTrainFault = new HashMap<>();

    //车辆基本详情
    private static final HashMap<String, String> trainInfoBase = new HashMap<>();


    //车辆hvac
    private static final HashMap<String, String> trainInfoHvac = new HashMap<>();


    //上一次插入的位置
    private static int trainInfoHvacListIdx = 0;


    //主题
    private static final String topic_ddu = "ddu";

    private static final String topic_train_card = "traincard";

    private static final String topic_fault = "fault";

    private static final String train_info_base = "traininfo_base";

    private static final String train_info_hvac = "traininfo_hvac";


    public HashMap<String, String> getResDdu() {
        return resDdu;
    }

    public HashMap<String, String> getResTrainCard() {
        return resTrainCard;
    }

    public HashMap<String, String> getResTrainFault() {
        return resTrainFault;
    }

    public HashMap<String, String> getTrainInfoBase() {
        return trainInfoBase;
    }

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

    // ddu页面
    @KafkaListener(id = "", topics = topic_ddu, groupId = "group.ddu")
    public void listenerDdu(ConsumerRecord<?, ?> record) {
        if (resDdu.containsKey("" + record.key())) {
            resDdu.replace("" + record.key(), "" + record.value());
        } else {
            resDdu.put("" + record.key(), "" + record.value());
        }
    }


    // 车辆卡片的MQ字符串处理，这个的处理可以参考空调页面后续做一些改进
    public Map<String, String> processTrainCardStr(String str) {
//        String str = "{sign_intensity=, next_station=车辆段, trainSpeed=0, drive_model=OFF, current_station=车辆段, control_model=}";
        int idx_a = 0, idx_b = 0;
        Map<String, String> res = new HashMap<>();
        ArrayList<String> model = new ArrayList<>();
        model.add("sign_intensity");
        model.add("next_station");
        model.add("trainSpeed");
        model.add("drive_model");
        model.add("current_station_idx");
        model.add("control_model_idx");
        for (int i = 0; i < 5; ++i) {
            idx_a = str.indexOf('=', idx_a + 1);
            idx_b = str.indexOf(',', idx_b + 1);
            res.put(model.get(i), str.substring(idx_a + 1, idx_b));
        }
        res.put(model.get(5), str.substring(str.indexOf('=', idx_a + 1) + 1, str.length() - 1));
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String s = "" + sdf.format(date);
        try {
            date = sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.put("updateDate", date.toString());
        return res;
    }


    //故障的字符串处理，得到hasmap类型
    public Map<String, String> processFaultStr(HashMap<String, String> resTrainFault) {
        Map<String, String> faultLevel = new HashMap<>();
        Iterator<String> iterator = resTrainFault.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String s = resTrainFault.get(key);
            if (s.contains("重大故障")) {
                faultLevel.put(key, "重大");
            } else if (s.contains("中度故障")) {
                faultLevel.put(key, "中度");
            } else {
                faultLevel.put(key, "轻微");
            }
        }
        return faultLevel;
    }


    // train_card页面
    @KafkaListener(id = "", topics = topic_train_card, groupId = "group.card")
    public void listenerCard(ConsumerRecord<?, ?> record) {

        if (resTrainCard.containsKey("" + record.key())) {
//            String s = (String) record.value();
//            Map<String, String> jsonMap = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
//            System.out.println("jsonMap: " + jsonMap.toString());
            resTrainCard.replace("" + record.key(), "" + record.value());
        } else {
            resTrainCard.put("" + record.key(), "" + record.value());
        }
    }

    // train_fault页面
    @KafkaListener(id = "", topics = topic_fault, groupId = "group.fault")
    public void listenerFault(ConsumerRecord<?, ?> record) {
        if (resTrainFault.containsKey("" + record.key())) {
            resTrainFault.replace("" + record.key(), "" + record.value());
        } else {
            resTrainFault.put("" + record.key(), "" + record.value());
        }
    }


    // train_info_base页面
    @KafkaListener(id = "", topics = train_info_base, groupId = "group.base")
    public void listenerTrainInfo(ConsumerRecord<?, ?> record) {
        if (trainInfoBase.containsKey("" + record.key())) {
            trainInfoBase.replace("" + record.key(), "" + record.value());
        } else {
            trainInfoBase.put("" + record.key(), "" + record.value());
        }
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

//        Date date = new Date();
//        if (date.getMinutes() - insertListDate.get(trainInfoHvacListIdx).getMinutes() >= 5) {
//            trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
//            trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //下一次更新的位置
//            insertListDate.set(trainInfoHvacListIdx, date);
//
//        } else if (date.getMinutes() - insertListDate.get(trainInfoHvacListIdx).getMinutes() < 0) {
//            if ((date.getMinutes() + (60 - insertListDate.get(trainInfoHvacListIdx).getMinutes())) >= 5) {
//                trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
//                trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //下一次更新的位置
//                insertListDate.set(trainInfoHvacListIdx, date);
//            }
//        }
        Date date = new Date();
        trainInfoHvacList.set(trainInfoHvacListIdx, trainInfoHvac);
        trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 25;  //下一次更新的位置
        insertListDate.set(trainInfoHvacListIdx, date);

    }


    public static Map<String, String> processTrainCardHavc(String str) {
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

    public Map<String, List<String>> getTemList(List<HashMap<String, String>> trainCardHvacList, String trainKey) { //25个数据，一个数据间隔5分钟
        String temList[] = {"returndampertemp", "senddampertemp", "idampertemp", "cooltemp", "inhaletemp", "exhausttemp", "outevaporationtemp", "evaporationtemp", "targettemp"};
        Map<String, List<String>> temResList = new HashMap<>();
        System.out.println(trainCardHvacList.get(0));
        if (trainCardHvacList.get(0).containsKey(trainKey)) {    //如果包含这辆列车
            Map<String, String> trainKeyCardMap = processTrainCardHavc(trainCardHvacList.get(0).get(trainKey).toString());
            for (Map.Entry<String, String> entry : trainKeyCardMap.entrySet()) {
//                System.out.println(entry.getKey().toString());
                for (int i = 0; i < temList.length; ++i) {
                    if (entry.getKey().toString().contains(temList[i])) {     //需要将这个字段变为数组
                        List<String> temp = new ArrayList<>();
                        for (int j = 0; j < trainCardHvacList.size(); ++j) {
                            if (trainCardHvacList.get(j).size() == 0) {
                                temp.add("0");
                            } else {
                                Map<String, String> trainCardMap = processTrainCardHavc(trainCardHvacList.get(j).get(trainKey).toString());
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
