package com.example.kafka_test.websocket;

import javax.websocket.Session;


public class OneThread extends Thread {
    private Session session;


//    public static void GetTid(String tid){
//        tid = tid;
//    }

    public OneThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
//        while (true) {
//            try {
//                String lineNum = "1";
//                String trainNum = "1";
//                System.out.println();
//                session.getBasicRemote().sendText(kafkaSendService.res_without_blank.toString());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }


}