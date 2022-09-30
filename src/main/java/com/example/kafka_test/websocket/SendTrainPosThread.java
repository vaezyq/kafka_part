package com.example.kafka_test.websocket;

import com.alibaba.fastjson.JSON;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.dto.TrainLocationAndTheta;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class SendTrainPosThread extends Thread {

    // websocket的会话
    private Session session;

//    DduDao dduDao = null;

    static ApplicationContext applicationContext = SpringUtil.getApplicationContext();

    static TrainCardDao trainCardDao = applicationContext.getBean(TrainCardDao.class);


    public TrainCardDao getTrainCardDao() {
        return trainCardDao;
    }

    public void setTrainCardDao(TrainCardDao trainCardDao) {
        SendTrainPosThread.trainCardDao = trainCardDao;
    }


    //构造函数
    public SendTrainPosThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        TrainCardDao trainCardDao = applicationContext.getBean(TrainCardDao.class);   //获取Spring注解管理的类对象
        while (true) {
            try {
                ArrayList<Map<String, Object>> res = trainCardDao.getTrainPositionMap();
                if (res.size() == 0) {
//                    Map<String, String> response = new LinkedHashMap<>();
//                    response.put("code", "400");
//                    response.put("msg", "no data");
//                    response.put("data", res.toString());

                    String jsonString2 = JSON.toJSONString(new MyResponseBody("400", "no data", res));
                    session.getBasicRemote().sendText(jsonString2);
                } else {
//                    Map<String, String> response = new LinkedHashMap<>();
//                    response.put("code", "200");
//                    response.put("msg", "sucess");
//                    response.put("data", res.toString());

                    String jsonString2 = JSON.toJSONString(new MyResponseBody("200", "success", res));
                    session.getBasicRemote().sendText(jsonString2);
//                    session.getBasicRemote().sendText(response.toString());
//                    session.getBasicRemote().sendText(response.toString());
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
