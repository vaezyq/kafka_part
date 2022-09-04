package com.example.kafka_test.dao;

import com.example.kafka_test.utils.ListenerTrainInfoHvacThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;


@Component
public class TrainInfoHvacDao {

    static ListenerTrainInfoHvacThread listenerThread = new ListenerTrainInfoHvacThread();

    static {
        listenerThread.start();
    }

    public HashMap<String, Map<String, String>> getTrainInfoHvac() {
//        System.out.println(trainInfoHvac);
        return listenerThread.getTrainInfoHvac();
    }

    public List<HashMap<String, Map<String, String>>> getTrainInfoHvacList() {

        return listenerThread.getTrainInfoHvacList();
    }

    public Map<String, List<String>> getTemList(List<HashMap<String, Map<String, String>>> trainCardHvacList, String trainKey) throws ParseException {
        return listenerThread.getTemList(trainCardHvacList,trainKey);
    }


//    @Autowired
//    private KafkaTemplate kafkaTemplate;

//    @Autowired
//    ProcessKafkaRecordUtils processKafkaRecordUtils;

    //车辆hvac
//    public static final HashMap<String, Map<String, String>> trainInfoHvac = new HashMap<>();
//
//    //上一次插入的位置
//    private static int trainInfoHvacListIdx = 0;
//
//    public static int getTrainInfoHvacListIdx() {
//        return trainInfoHvacListIdx;
//    }
//
//    public static final String train_info_hvac = "traininfo_hvac";
//
//
//    //因为目前一秒插入两个，然后显示时一秒两个，所以间隔
//    private List<HashMap<String, Map<String, String>>> trainInfoHvacList = new ArrayList<HashMap<String, Map<String, String>>>() {{
//        for (int i = 0; i < 50; ++i) {
//            HashMap<String, Map<String, String>> hashMap = new HashMap<>();
//            add(hashMap);
//        }
//    }};
//
//    //每一次插入的时间
//    private List<Date> insertListDate = new ArrayList<Date>() {{
//        for (int i = 0; i < 25; ++i) {
//            add(new Date());
//        }
//    }};
//
//    public List<Date> getInsertListDate() {
//        return insertListDate;
//    }






//    // train_info_hvac页面
//    @KafkaListener(id = "", topics = train_info_hvac, groupId = "new_12")
//    public void listenerTrainInfoHvac(ConsumerRecord<?, ?> record) {
//
//        if (trainInfoHvac.containsKey(record.key().toString().substring(0, 4))) {
//            trainInfoHvac.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        } else {
//            trainInfoHvac.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        }
//        System.out.println(trainInfoHvac.get(record.key().toString().substring(0, 4)).get("date"));
//        System.out.println(record.key());
//        HashMap<String, Map<String, String>> res = new HashMap<>(trainInfoHvac);
//
//        trainInfoHvacList.set(trainInfoHvacListIdx, res);
//        trainInfoHvacListIdx = (trainInfoHvacListIdx + 1) % 50;
////        System.out.println(trainInfoHvacListIdx);
////        System.out.println(record.key());
//
//    }


//    @PostConstruct
//    public void listenerTrainInfoHvac() {
//        ListenerThread listenerThread = new ListenerThread();
//        listenerThread.start();
//    }


}
