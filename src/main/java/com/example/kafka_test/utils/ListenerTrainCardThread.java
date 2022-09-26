package com.example.kafka_test.utils;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dto.TrainLine;
import com.example.kafka_test.service.TrainLinesService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListenerTrainCardThread extends Thread {

    @Autowired
    TrainLinesService trainLinesService;

    KafkaProperties kafkaProperties = new KafkaProperties();

    RecordStringProcess recordStringProcess = new RecordStringProcess();

    /*
    用于存储上次收到的车辆卡片
      上次 某车辆的车辆卡片 与 当前该车辆的车辆卡片 的 当前站 比较 判断是否到站
      若未到站则
        上次 某车辆的车辆卡片 与 当前该车辆的车辆卡片 的 date 比较，若在同一秒则忽略
        若不在同一秒则
          取平均速度与时间相乘，与已经走过的距离累加，计算rate
     */
    HashMap<String,Map<String,String>> lastReceiveCards = null;
    Map<String,Double> currentDistance = null;

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

    @Override
    public void run() {
        //车辆卡片部分的消费者
        KafkaConsumer<String, String> kafkaConsumer_trainCard = new KafkaConsumer<>(kafkaProperties.getProperties());
        kafkaConsumer_trainCard.subscribe(Arrays.asList(topic_train_card));
        while (true) {
            //车辆卡片部分的数据处理
            ConsumerRecords<String, String> records_train_card = kafkaConsumer_trainCard.poll(500);
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
            }

//            //todo 根据车辆卡片信息计算位置
//            //初次启动时，lastReceiveCard为空
//            if(lastReceiveCards==null){
//                //需要对lastReceiveCard进行赋值
//                lastReceiveCards=resTrainCard;
//                //需要对currentDistance进行初始化
//                Iterator<String> iterator = resTrainCard.keySet().iterator();
//                while(iterator.hasNext()){
//                    currentDistance.put(iterator.next(),0.0);
//                }
//            }
//            //如果收到的不是第一张车辆卡片，需要结合上一张车辆卡片进行处理
//            else {
//                Iterator<String> iterator = resTrainCard.keySet().iterator();
//
//                //首先遍历当前车辆卡片集合中每一辆列车的卡片
//                while(iterator.hasNext()){
//                    double rate = 0.0;
//                    String source;
//                    String target;
//                    //获取到当前的某一辆列车的列车号
//                    String trainNum = iterator.next();
//                    //如果是新加入的列车,则无法估算准确位置，初始化
//                    if(!lastReceiveCards.containsKey(trainNum)){
//                        Map<String,String> map = removeKeySpace(resTrainCard.get(trainNum));
//                        currentDistance.put(trainNum,0.0);
//                        lastReceiveCards.put(trainNum,map);
//                        source = map.get("current_station");
//                        target = map.get("next_station");
//                    }
//                    else{ //如果之前已经收到了车辆的卡片
//                        //拿到对应的当前车辆卡片
//                        Map<String,String> trainCardMapNow = removeKeySpace(resTrainCard.get(trainNum));
//                        source = trainCardMapNow.get("current_station");
//                        target = trainCardMapNow.get("next_station");
//                        //拿到对应的上次车辆卡片
//                        Map<String,String> trainCardMapLast = removeKeySpace(lastReceiveCards.get(trainNum));
//                        //如果车辆已经到了下一站
//                        if(source!=trainCardMapLast.get("current_station")){
//                            currentDistance.replace(trainNum,0.0);
//                        }
//                        else { //如果没有到达下一站
//                            //先获取卡片时间
//                            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date thisTime;
//                            Date lastTime;
//                            try {
//                                thisTime = timeFormat.parse(trainCardMapNow.get("date"));
//                                lastTime = timeFormat.parse(trainCardMapLast.get("date"));
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            HashMap<TrainLine, Double> trainLineDoubleHashMap = trainLinesService.getTrainLineDoubleHashMap();
//                            //两次时间不同时，currentDistance更新
//                            if(thisTime!=lastTime){
//                                //currentDistance更新的数据准备
//                                //计算时间差,单位是秒
//                               long runTime = (thisTime.getTime()-lastTime.getTime())/1000;
//                               //计算平均速度 单位未知 希望是m/s
//                               double avgSpeed = (Double.valueOf(trainCardMapNow.get("trainSpeed")) + Double.valueOf(trainCardMapLast.get("trainSpeed")))/2;
//                               //在lastReceiveCards中存在的话，currentDistance内一定有该车辆信息
//                               double newDistance = currentDistance.get(trainNum) + runTime * avgSpeed;
//                               TrainLine tl = new TrainLine();
//                               tl.setSource(source);
//                               tl.setTarget(target);
//                               double totalDistance = trainLineDoubleHashMap.get(tl);
//
//                               //更新currentDistance
//                               //如果此时估算结果已超过，则取最大值的0.95，如果未超过，就累加估计值
//                               if(newDistance < totalDistance)
//                                   currentDistance.replace(trainNum,newDistance);
//                               else currentDistance.replace(trainNum,totalDistance * 0.95);
//
//                               //根据currentDistance，计算车辆当前的rate了
//                               rate = currentDistance.get(trainNum)/totalDistance;
//                            }else{ //两次时间相同时，currentDistance不更新
//                                TrainLine tl = new TrainLine();
//                                tl.setSource(source);
//                                tl.setTarget(target);
//                                double totalDistance = trainLineDoubleHashMap.get(tl);
//                                rate = currentDistance.get(trainNum)/totalDistance;
//                            }
//                        }
//                        lastReceiveCards.replace(trainNum,removeKeySpace(resTrainCard.get(trainNum)));
//                    }
//                    //借助service计算坐标和方向
//                    double[] coordinate = trainLinesService.calculateCoordinate(source,target,rate);
//                    double theta = trainLinesService.calculateDirection(source,target);
//                }
//
//            }

            //System.out.println(resTrainCard);

        }
    }

}
