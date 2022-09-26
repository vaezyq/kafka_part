package com.example.kafka_test.websocket;

import com.example.kafka_test.dao.DduDao;
import com.example.kafka_test.service.TrainInfoHvacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/airCond")
@Component
public class AirCondWebSocket {

    @Autowired
    TrainInfoHvacService trainInfoHvacService;


    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<AirCondWebSocket> webSocketSet = new CopyOnWriteArraySet<AirCondWebSocket>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    SendAirCondThread sendAirCondThread = null;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线数加1
        System.out.println("有新连接加入！当前在线人数为 : " + getOnlineCount());
        try {
            sendMessage("您已成功连接！");
            sendAirCondThread = new SendAirCondThread(session);
            String lineNum = "7";
            String trainNum = "4";
            sendAirCondThread.setLineNum(lineNum);
            sendAirCondThread.setTrainNum(trainNum);
            sendAirCondThread.setTrainInfoHvacService(trainInfoHvacService);
            sendAirCondThread.start();
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        sendAirCondThread.stop();
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
        System.out.println("有一连接关闭！当前在线人数为 : " + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        int idx = message.indexOf(' ');
        String lineNum = message.substring(0, idx);
        String trainNum = message.substring(idx + 1, message.length());
        sendAirCondThread.stop();
        sendAirCondThread = new SendAirCondThread(session);
        sendAirCondThread.setLineNum(lineNum);
        sendAirCondThread.setTrainNum(trainNum);
        sendAirCondThread.setTrainInfoHvacService(trainInfoHvacService);
        sendAirCondThread.start();

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        // this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (AirCondWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        AirCondWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        AirCondWebSocket.onlineCount--;
    }
}
