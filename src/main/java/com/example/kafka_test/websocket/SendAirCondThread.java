package com.example.kafka_test.websocket;

import com.example.kafka_test.dao.DduDao;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;
import java.text.ParseException;
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

                Map<String, String> res = trainInfoHvacService.getAirCondResult(lineNum, trainNum);
                if (res.size() == 0) {
                    session.getBasicRemote().sendText(new MyResponseBody("400", "no data", res).toString());
                } else {
                    session.getBasicRemote().sendText(new MyResponseBody("200", "success", res).toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
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
