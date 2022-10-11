package com.example.kafka_test.dao;

import com.example.kafka_test.dto.TrainLocationAndTheta;
import com.example.kafka_test.utils.ListenerTrainCardThread;
import com.example.kafka_test.utils.TrainLinesUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrainCardDao {

//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    static ListenerTrainCardThread listenerTrainCardThread = new ListenerTrainCardThread();

    static {
        listenerTrainCardThread.start();
    }


    public HashMap<String, Map<String, String>> getResTrainCard() {
        return listenerTrainCardThread.getResTrainCard();
    }

//    HashMap<String, TrainLocationAndTheta> webSocketResponseMap = new HashMap<String, TrainLocationAndTheta>();


    public ArrayList<Map<String, Object>> getTrainPositionMap() {

        ArrayList<Map<String, Object>> res = new ArrayList<>();


        HashMap<String, TrainLocationAndTheta> trainPosTemp = ListenerTrainCardThread.webSocketResponseMap;

        for (Map.Entry<String, TrainLocationAndTheta> entry : trainPosTemp.entrySet()) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("coordsData", entry.getValue().getCoordsData());
            temp.put("subwayNum", entry.getValue().getSubwayNum());
            temp.put("symbolRotateNum", entry.getValue().getSymbolRotateNum());
//            System.out.println(listenerTrainCardThread.getResTrainCard());

            String speed = listenerTrainCardThread.getResTrainCard().get(entry.getValue().getSubwayNum()).get("trainspeed");
            temp.put("speed", Double.parseDouble(String.format("%.2f", Float.parseFloat(speed) / 10)));
//            System.out.println(listenerTrainCardThread.getResTrainCard().get(entry.getValue().getSubwayNum()));

            String nextStationId = listenerTrainCardThread.getResTrainCard().get(entry.getValue().getSubwayNum()).get("next_station");
            TrainLinesUtils tu = new TrainLinesUtils();
            temp.put("nextStation", tu.queryStationNameById(nextStationId));
            //temp.put("nextStation", Integer.parseInt(listenerTrainCardThread.getResTrainCard().get(entry.getValue().getSubwayNum()).get("next_station")));

            temp.put("pressure", Integer.parseInt(listenerTrainCardThread.getResTrainCard().get(entry.getValue().getSubwayNum()).get("mainairpressure")));
            System.out.println(temp);
            res.add(temp);
//            System.out.println(temp);
        }

        return res;
    }


//    // train_card页面
//    @KafkaListener(id = "", topics = topic_train_card, groupId = "new_12")
//    public void listenerCard(ConsumerRecord<?, ?> record) {
//
//        if (record.key().toString().substring(0, 4).equals("7005")) {
//            return;
//        }
//        if (resTrainCard.containsKey(record.key().toString().substring(0, 4))) {
////            String s = (String) record.value();
////            Map<String, String> jsonMap = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
////            System.out.println("jsonMap: " + jsonMap.toString());
//            resTrainCard.replace(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        } else {
//            resTrainCard.put(record.key().toString().substring(0, 4), processKafkaRecordUtils.processRecordAndString(record.key().toString(), record.value().toString()));
//        }
//    }

}
