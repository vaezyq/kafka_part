package com.example.kafka_test.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainCardDao {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 车辆卡片
    private static final HashMap<String, String> resTrainCard = new HashMap<>();

    private static final String topic_train_card = "traincard";

    public HashMap<String, String> getResTrainCard() {
        return resTrainCard;
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


}
