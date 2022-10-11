package com.example.kafka_test;

import com.example.kafka_test.dto.LineInfo;
import org.w3c.dom.UserDataHandler;

import javax.swing.*;
import java.io.PrintStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class test {

//    public static Date getNowDate() {
//
//        return currentTime_2;
//    }

    public static void main(String[] args) throws ParseException, SQLException {
//        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
////        sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//////        String date = sdf1.format(new Date());
//////        System.out.println(sdf1.format(new Date()));
////        String str = "{mp2Hvac1S2CoolTemp=, mp1Hvac2Ventilation2State=, M2Hvac2IExtTemp=63, mp1Hvac2ITargetTemp=56, M2Hvac1S1SendDamperTemp=}";
//////        System.out.println(processTrainCardHavc(str));
        List<LineInfo> list = new ArrayList<LineInfo>();
        String dirver = "com.mysql.jdbc.Driver";
        String user = "root";
        String psd = "123456";
        String database = "train_card";
        String url = "jdbc:mysql://121.4.90.236:3306/train_card?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true" + "&user=" + user + "&password=" + psd;
        Connection conn = DriverManager.getConnection(url);
        Statement stat = conn.createStatement();
        String sql1 = "select * from  line_content";
        ResultSet rs1 = stat.executeQuery(sql1);
        rs1.next();
//        System.out.println(rs1.getString("base"));
//        while (rs1.next()) {
////            System.out.println(rs1.getCursorName());
//
//        }
        rs1.close();
        stat.close();
        conn.close();

    }

    public static Map<String, String> processTrainCardHavc(String str) {


        Map<String, String> trainCardHavc = new HashMap<>();
        int comma_idx_fir = 0;
        int equal_idx = 0;
        int comma_idx_sec = -1; 
        int count = 0;
        while ((comma_idx_sec = str.indexOf(',', comma_idx_sec + 1)) != -1) {
            equal_idx = str.indexOf('=', equal_idx + 1);
            count++;
//            System.out.println(comma_idx_fir);
//            System.out.println(equal_idx);
//            System.out.println(comma_idx_sec);
//            System.out.println(count);
            String key = str.substring(comma_idx_fir + 1, equal_idx);
            String value = str.substring(equal_idx + 1, comma_idx_sec);
            trainCardHavc.put(key, value);
            comma_idx_fir = comma_idx_sec;
        }
        equal_idx = str.indexOf('=', equal_idx + 1);
//        System.out.println(comma_idx_fir);
//        System.out.println(equal_idx);
//        System.out.println(str.length());

        trainCardHavc.put(str.substring(comma_idx_fir + 1, equal_idx), str.substring(equal_idx + 1, str.length() - 1));
        return trainCardHavc;
    }
}
