package com.example.kafka_test.websocket;

import com.alibaba.fastjson.JSON;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainInfoBaseService;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class SendTrainInfoBaseThread extends Thread {

    // websocket的会话
    private Session session;

//    DduDao dduDao = null;

    static ApplicationContext applicationContext = SpringUtil.getApplicationContext();

    static TrainInfoBaseService trainInfoBaseService = applicationContext.getBean(TrainInfoBaseService.class);//获取Spring注解管理的类对象


    public TrainInfoBaseService getTrainInfoBaseService() {
        return trainInfoBaseService;
    }

    public static void setTrainInfoBaseService(TrainInfoBaseService trainInfoBaseService) {
        SendTrainInfoBaseThread.trainInfoBaseService = trainInfoBaseService;
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
    public SendTrainInfoBaseThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {


        TrainInfoBaseService trainInfoBaseService = applicationContext.getBean(TrainInfoBaseService.class);//获取Spring注解管理的类对象


        while (true) {
            try {

                Map<String, Object> res = trainInfoBaseService.getTrainBaseInfo(lineNum, trainNum);

                if (res.size() == 0) {
                    String jsonString2 = JSON.toJSONString(new MyResponseBody("400", "no data", res));
                    session.getBasicRemote().sendText(jsonString2);
                } else {
                    String jsonString2 = JSON.toJSONString(new MyResponseBody("200", "success", res));
                    session.getBasicRemote().sendText(jsonString2);
                }
            } catch (IOException e) {
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
