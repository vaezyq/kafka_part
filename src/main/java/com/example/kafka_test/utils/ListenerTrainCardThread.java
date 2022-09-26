package com.example.kafka_test.utils;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class ListenerTrainCardThread extends Thread {

    KafkaProperties kafkaProperties = new KafkaProperties();

    RecordStringProcess recordStringProcess = new RecordStringProcess();

    // 车辆卡片
    private static final HashMap<String, Map<String, String>> resTrainCard = new HashMap<>();

    public HashMap<String, Map<String, String>> getResTrainCard() {
        return resTrainCard;
    }

    private static final String topic_train_card = "traincard";


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


    @Override
    public void run() {
        //车辆卡片部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_trainCard = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_trainCard.subscribe(Arrays.asList(topic_train_card));
        while (true) {
            //车辆卡片部分的数据处理
            ConsumerRecords<String, String> records_train_card = kafkaConsumer_trainCard.poll(500);
//            System.out.println(records_train_card.toString());
            for (ConsumerRecord<String, String> record : records_train_card) {
                //7005列车现在数据存在问题,所以直接舍弃掉，这个后续更改
//                if (record.key().toString().substring(0, 4).equals("7005")) {
//                    return;
//                }
                if (resTrainCard.containsKey(record.key().toString().substring(0, 4))) {
                    resTrainCard.replace(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                } else {
                    resTrainCard.put(record.key().toString().substring(0, 4), recordStringProcess.processRecordAndString(record.key().toString(), record.value().toString()));
                }
//                System.out.println(record.value());
            }
//            System.out.println(resTrainCard);
        }
    }
}
