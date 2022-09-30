package com.example.kafka_test.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kafka_test.dao.DduDao;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SendAirCondThread extends Thread {

    // websocket的会话
    private Session session;

//    DduDao dduDao = null;

    static ApplicationContext applicationContext = SpringUtil.getApplicationContext();

    static TrainInfoHvacService trainInfoHvacService = applicationContext.getBean(TrainInfoHvacService.class);//获取Spring注解管理的类对象

//    static {
//        System.out.println(dduDao);
//        System.out.println("-----------");
//        System.out.println(dduDao.getResDdu());
//    }

    ;//调用类的方法


    public TrainInfoHvacService getTrainInfoHvacService() {
        return trainInfoHvacService;
    }

    public void setTrainInfoHvacService(TrainInfoHvacService trainInfoHvacService) {
        SendAirCondThread.trainInfoHvacService = trainInfoHvacService;
    }

    private String lineNum;

    private String trainNum;

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    //构造函数
    public SendAirCondThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        TrainInfoHvacService trainInfoHvacService = applicationContext.getBean(TrainInfoHvacService.class);//获取Spring注解管理的类对象
//            System.out.println();
//            System.out.println(dduDao);
        while (true) {
            try {

                Map<String, Object> res = trainInfoHvacService.getAirCondResult(lineNum, trainNum);
                if (res.size() == 0) {

//                    Map<String, String> response = new LinkedHashMap<>();
//                    response.put("code", "400");
//                    response.put("msg", "no data");
//                    response.put("data", res.toString());

                    String jsonString2 = JSON.toJSONString(new MyResponseBody("400","no data",res));
                    session.getBasicRemote().sendText(jsonString2);
                } else {
//                    Map<String, String> response = new LinkedHashMap<>();
//                    response.put("code", "200");
//                    response.put("msg", "sucess");
//                    response.put("data", res.toString());

                    String jsonString2 = JSON.toJSONString(new MyResponseBody("200","no data",res));
                    session.getBasicRemote().sendText(jsonString2);
//                    session.getBasicRemote().sendText(response.toString());
//                    session.getBasicRemote().sendText(response.toString());
                }
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
