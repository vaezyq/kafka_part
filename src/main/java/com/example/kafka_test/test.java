package com.example.kafka_test;

import org.w3c.dom.UserDataHandler;

import javax.swing.*;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) throws ParseException {

        String str = "{mp2Hvac1S2CoolTemp=, mp1Hvac2Ventilation2State=, M2Hvac2IExtTemp=63, mp1Hvac2ITargetTemp=56, M2Hvac1S1SendDamperTemp=}";
        System.out.println(processTrainCardHavc(str));
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
