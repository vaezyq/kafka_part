package com.example.kafka_test.websocket;

import com.example.kafka_test.dao.DduDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;

public class SendDduThread extends Thread {

    // websocket的会话
    private Session session;



//    DduDao dduDao = null;

    static ApplicationContext applicationContext = SpringUtil.getApplicationContext();

    static DduDao dduDao = applicationContext.getBean(DduDao.class);//获取Spring注解管理的类对象




    static {
        System.out.println(dduDao);
        System.out.println(dduDao.getResDdu());
    }

    ;//调用类的方法


    public DduDao getDduDao() {
        return dduDao;
    }

    public void setDduDao(DduDao dduDao) {
        this.dduDao = dduDao;
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
    public SendDduThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            dduDao = applicationContext.getBean(DduDao.class);//获取Spring注解管理的类对象
//            System.out.println(dduDao);
            try {
//                System.out.println(dduDao);
                session.getBasicRemote().sendText(dduDao.getResDdu().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

//        y=f(x)  600
// 1 [2,5]        [1.5,5.5]                    2   [1,6]
