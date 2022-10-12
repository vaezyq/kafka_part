package com.example.kafka_test.utils;

import com.example.kafka_test.dto.TrainLocationAndTheta;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListenerTrainCardThread extends Thread {

    //@Autowired
    TrainLinesUtils trainLinesUtils = new TrainLinesUtils();

    KafkaProperties kafkaProperties = new KafkaProperties();

    RecordStringProcess recordStringProcess = new RecordStringProcess();


    //上次收到的车辆卡片集合
    HashMap<String,Map<String,String>> lastReceiveCards = new HashMap<String,Map<String,String>>();
    //用于保存当前各个车辆在各段线路上行驶距离的hashmap
    HashMap<String,Double> currentDistance = new HashMap<String,Double>();
    //用于保存返回websocket内容的结构
    static public HashMap<String, TrainLocationAndTheta> webSocketResponseMap = new HashMap<String, TrainLocationAndTheta>();

    double thetaWhenInStation = 0.0;

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

    public Map<String, String> removeKeySpace(Map<String, String> map) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().indexOf(" ") == 0) {  //空格都是开头第一个
                res.put(entry.getKey().trim(), entry.getValue().toString());
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
//                System.out.println(record.key());
//                System.out.println(record.value());
            }



            //拿到车辆卡片后，先删掉其中的空格
            //发现不论是7004还是7005 都会有时接收的数据有空格，有时接收的数据没空格
            if(resTrainCard!=null && !resTrainCard.isEmpty()){
                Iterator<String> iterator = resTrainCard.keySet().iterator();
                while(iterator.hasNext()){
                    String trainNum = iterator.next();
                    // System.out.println(trainNum+"删除空格前："+resTrainCard.get(trainNum));
                    resTrainCard.replace(trainNum ,removeKeySpace(resTrainCard.get(trainNum)));
                    // System.out.println(trainNum+"删除空格后："+resTrainCard.get(trainNum));
                }
            }

            /*
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            System.out.println("resTrainCard为："+resTrainCard);
            System.out.println("lastReceiveCards为"+lastReceiveCards);
            System.out.println("currentDistance为"+currentDistance);
            */

            //todo 根据车辆卡片信息计算位置
            //初次启动时，lastReceiveCard为空,若拿到了第一个resTrainCard
            if(lastReceiveCards.isEmpty() && !resTrainCard.isEmpty() ){
                //需要对lastReceiveCard进行赋值
                /*
                System.out.println("lastReceivedCards为null或空,要被resTrainCard赋值");
                */
                Iterator<String> iterator = resTrainCard.keySet().iterator();
                while(iterator.hasNext()){
                    String trainNum = iterator.next();
                    lastReceiveCards.put(trainNum,resTrainCard.get(trainNum));
                    //需要对currentDistance进行初始化
                    currentDistance.put(trainNum,0.0);
                }
                /*
                System.out.println("赋值后，lastReceivedCards为："+lastReceiveCards);
                */
                //return lastReceivedCard为空前不推送websocket
            }

            //如果收到的不是第一张车辆卡片，需要结合上一张车辆卡片进行处理
            else {
                Iterator<String> iterator = resTrainCard.keySet().iterator();
                //首先遍历当前车辆卡片集合中每一辆列车的卡片
                while(iterator.hasNext()){
                    double rate = 0.0;
                    //获取到当前的某一辆列车的列车号
                    String trainNum = iterator.next();

                    //System.out.println(" ");
                    //System.out.println("此时处理的车辆号为"+trainNum);

                    Map<String,String> map = resTrainCard.get(trainNum);
                    String current_station = map.get("current_station");
                    String next_station = map.get("next_station");
                    if (trainLinesUtils.queryPositionById(current_station)==null || trainLinesUtils.queryPositionById(next_station)==null){
                        //System.out.println("该车辆目前不位于7号线运行区间");
                    }
                    else if(current_station.equals(next_station)){
                        //System.out.println("此时列车位于"+current_station+"站");
                        //return 列车到站期间直接返回车站坐标
                        double[] coordinate = trainLinesUtils.queryPositionById(current_station);
                        double theta = thetaWhenInStation;
                        //System.out.println(trainNum+"列车当前坐标为:"+"["+coordinate[0]+","+coordinate[1]+"],角度为"+theta+"度。 ");
                        webSocketResponseMap.put(trainNum,new TrainLocationAndTheta(coordinate,trainNum,theta));
                    }
                    else{
                        //System.out.println("该车辆目前正从"+current_station+"号站前往"+next_station+"号站");
                        //如果是新收到的列车,则无法估算准确位置，初始化
                        if(!lastReceiveCards.containsKey(trainNum)){
                            //System.out.println("该车辆在lastReceiveCards里找不到，将其添加进去");
                            currentDistance.put(trainNum,0.0);
                            //System.out.println("添加前lastReceiveCards为"+lastReceiveCards);
                            lastReceiveCards.put(trainNum,map);
                            //System.out.println("添加后lastReceiveCards为"+lastReceiveCards);
                            //return 此时直接置于当前车站，直到收到车辆卡片通知：已到达下一站，才能有准确位置
                            double[] coordinate = trainLinesUtils.queryPositionById(current_station);
                            double theta = trainLinesUtils.calculateDirection(current_station,next_station);
                            webSocketResponseMap.put(trainNum,new TrainLocationAndTheta(coordinate,trainNum,theta));
                            //System.out.println(trainNum+"列车当前坐标为:"+"["+coordinate[0]+","+coordinate[1]+"],角度为:"+theta+"度。 ps：新加入车辆,不准确");
                        }

                        else if (lastReceiveCards.containsKey(trainNum)){ //如果之前已经收到了车辆的卡片
                            //System.out.println("该车辆在lastReceiveCards里存在：");
                            //拿到对应的上次车辆卡片
                            Map<String,String> trainCardMapLast = lastReceiveCards.get(trainNum);
                            String lastCurrentStation = trainCardMapLast.get("current_station");
                            //System.out.println("上次"+trainNum+"号列车的车辆卡片如下:"+trainCardMapLast);
                            //System.out.println("而这次"+trainNum+"号列车的车辆卡片如下"+map);
                            //如果车辆已经到了下一站
                            if(!current_station.equals(lastCurrentStation)){
                                //System.out.println("上次车辆卡片的当前站为"+lastCurrentStation+",而这次车辆卡片的当前站为"+current_station);
                                //System.out.println("当前卡片和上一张卡片的当前站发生变化了，认为新到站");
                                currentDistance.replace(trainNum,0.0);
                                lastReceiveCards.replace(trainNum,map);
                                //return websocket发送坐标为current_station的坐标
                                double theta = trainLinesUtils.calculateDirection(lastCurrentStation,current_station);
                                //到站期间，需要保存之前的方向角度
                                thetaWhenInStation = theta;
                                double[] coordinate = trainLinesUtils.queryPositionById(current_station);
                                //System.out.println(trainNum+"列车当前坐标为:"+"["+coordinate[0]+","+coordinate[1]+"],角度为:"+theta+"度。  ps：已到站");
                                webSocketResponseMap.put(trainNum,new TrainLocationAndTheta(coordinate,trainNum,theta));
                            }
                            else { //如果没有到达下一站
                                //先获取卡片时间
                                //System.out.println("当前卡片和上一张卡片的当前站没有发生变化，正在当前线路上运行");
                                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date thisTime;
                                Date lastTime;
                                try {
                                    thisTime = timeFormat.parse(map.get("date"));
                                    lastTime = timeFormat.parse(trainCardMapLast.get("date"));
                                    //System.out.println("thisTime为"+thisTime);
                                    //System.out.println("lastTime为"+lastTime);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if(thisTime.equals(lastTime)){
                                    //System.out.println("两次车辆卡片的时间相同，没有推送,直接舍去");
                                }
                                else{
                                    //System.out.println("两次车辆卡片的时间不同");
                                    double totalDistance = trainLinesUtils.queryDistanceById(current_station,next_station);
                                    double stopDistance = totalDistance * 0.95;
                                    if (currentDistance.get(trainNum)<stopDistance){
                                        //System.out.println("此时没有到达预测缓冲点");
                                        //currentDistance更新的数据准备
                                        //计算时间差,单位是秒
                                        long runTime = (thisTime.getTime()-lastTime.getTime())/1000;
                                        //System.out.println("时间差为"+runTime);
                                        //计算平均速度 单位未知 希望是m/s
                                        double lastSpeed = Double.parseDouble(trainCardMapLast.get("trainspeed"))/36;
                                        //System.out.println("上次速度为"+lastSpeed);
                                        double nowSpeed = Double.parseDouble(map.get("trainspeed"))/36;
                                        //System.out.println("这次速度为"+nowSpeed);
                                        double avgSpeed = (lastSpeed+nowSpeed)/2;
                                        //System.out.println("平均速度为"+avgSpeed);
                                        //System.out.println("之前已走进度为"+currentDistance.get(trainNum));
                                        //System.out.println("本次前进距离为"+runTime * avgSpeed);
                                        //在lastReceiveCards中存在的话，currentDistance内一定有该车辆信息
                                        double newDistance = currentDistance.get(trainNum) + runTime * avgSpeed;
                                        //System.out.println("更新新距离为"+newDistance);
                                        //更新currentDistance
                                        //如果此时估算结果已超过，则取最大值的0.95，如果未超过，就累加估计值
                                        if(newDistance < stopDistance){
                                            //System.out.println("未超过预测缓冲点"+stopDistance+",更新"+trainNum+"的currentDistance为"+newDistance);
                                            currentDistance.replace(trainNum,newDistance);
                                        }
                                        else {
                                            //System.out.println("更新距离已超过预测缓冲点,currentDistance定为"+stopDistance);
                                            currentDistance.replace(trainNum,stopDistance);
                                        }
                                    }else{
                                        //System.out.println("currentDistance已达到预测缓冲点，不更新");
                                    }

                                    //根据currentDistance，计算车辆当前的rate了
                                    rate = currentDistance.get(trainNum)/totalDistance;
                                    //System.out.println("rate取"+rate);
                                    lastReceiveCards.replace(trainNum,resTrainCard.get(trainNum));
                                    //todo websocket根据计算出的rate发送坐标
                                    double[] coordinate = trainLinesUtils.calculateCoordinate(current_station,next_station,rate);
                                    double theta = trainLinesUtils.calculateDirection(current_station,next_station);
                                    //System.out.println(trainNum+"列车当前坐标为:"+"["+coordinate[0]+","+coordinate[1]+"],角度为:"+theta+"度。  ps：正在运行");
                                    webSocketResponseMap.put(trainNum,new TrainLocationAndTheta(coordinate,trainNum,theta));
                                }
                            }
                        }
                    }


                }
            }
//            System.out.println(" ");
//            System.out.println("要返回的webSocket为"+webSocketResponseMap);


        }
    }
}
