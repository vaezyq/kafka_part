package com.example.kafka_test.service;

import com.example.kafka_test.dao.ProcessKafkaRecordUtils;
import com.example.kafka_test.dao.TrainCardDao;
import com.example.kafka_test.dao.TrainFaultDao;
import com.example.kafka_test.dto.LineInfo;
import com.example.kafka_test.dto.trainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service
public class TrainCardService {

    @Autowired
    TrainFaultDao trainFaultDao;

    @Autowired
    TrainCardDao trainCardDao;

    @Autowired
    ProcessKafkaRecordUtils processKafkaRecordUtils;

    static String dirver = "com.mysql.jdbc.Driver";
    static String user = "root";
    static String psd = "123456";
    String database = "train_card";
    static String ip = "121.4.90.236";
    static String url = "jdbc:mysql://" + ip + ":3306/train_card?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true" + "&user=" + user + "&password=" + psd;

    static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Statement stat;

    static {
        try {
            stat = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //得到线路信息的结果
    private static LineInfo getRes(ResultSet rs1) throws SQLException {
        LineInfo lineInfo = new LineInfo(rs1.getString("city"), rs1.getString("project"), rs1.getInt("lineNum"), rs1.getDate("insert_time"), rs1.getDate("collect_time"), rs1.getString("base"), rs1.getDate("opening_time"), rs1.getString("power"), rs1.getInt("station_num"), rs1.getInt("line_config_num"), rs1.getInt("total_line_length"), rs1.getInt("transfer_points_num"), rs1.getInt("transfer_line_num"), rs1.getInt("train_num"));
        return lineInfo;
    }

    //    1. 线路部分线路应该是主键(这里数据库还没有加)，所以只会查询到唯一的一个信息
    public static LineInfo getLineInfoById(String lineNum) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String sql1 = "select * from  line_content where lineNum= " + lineNum;
        ResultSet rs1 = stat.executeQuery(sql1);
        rs1.next();
        if (rs1.isLast()) {

        }
        LineInfo lineInfo = getRes(rs1);

//        System.out.println(rs1.getString("city"));
//        System.out.println(rs1.getString("project"));
//        System.out.println(rs1.getInt("lineNum"));
//        System.out.println(rs1.getDate("insert_time"));
//        System.out.println(rs1.getDate("collect_time"));
//        System.out.println(rs1.getString("base"));
//        System.out.println(rs1.getDate("opening_time"));
//        System.out.println(rs1.getString("power"));
//        System.out.println(rs1.getInt("station_num"));
//        System.out.println(rs1.getInt("line_config_num"));
//        System.out.println(rs1.getInt("total_line_length"));
//        System.out.println(rs1.getInt("transfer_points_num"));
//        System.out.println(rs1.getInt("transfer_line_num"));
//        System.out.println(rs1.getInt("train_num"));
        rs1.close();
        return lineInfo;
    }

    //    2. 得到所有线路信息
    public static List<LineInfo> getLineInfos() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String sql1 = "select * from  line_content";

        ArrayList<LineInfo> res = new ArrayList<>();

        ResultSet rs1 = stat.executeQuery(sql1);
        while (rs1.next()) {
            res.add(getRes(rs1));
        }
        rs1.close();
        return res;
    }


    //    3. 得到指定线路的列车信息
    public static List<LineInfo> getTrainInfos(String lineNum) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String sql1 = "select * from  line_content";

        ArrayList<LineInfo> res = new ArrayList<>();

        ResultSet rs1 = stat.executeQuery(sql1);
        while (rs1.next()) {
            res.add(getRes(rs1));

            rs1.close();
        }
        return res;
    }


    // 处理车辆卡片的字符串，返回map
    public Map<String, String> processTrainCardStr(String str) {
        int idx_a = 0, idx_b = 0;
        Map<String, String> res = new HashMap<>();
        ArrayList<String> model = new ArrayList<>();
        model.add("train_speed");
        model.add("sign_intensity");
        model.add("next_station");
        model.add("drive_model");
        model.add("current_station");
        model.add("control_model");
        for (int i = 0; i < 5; ++i) {
            idx_a = str.indexOf('=', idx_a + 1);
            idx_b = str.indexOf(',', idx_b + 1);
            res.put(model.get(i), str.substring(idx_a + 1, idx_b));
        }
        res.put(model.get(5), str.substring(str.indexOf('=', idx_a + 1) + 1, str.length() - 1));
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        String s = "" + sdf.format(date);
//        System.out.println(s);
//        try {
//            date = sdf.parse(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        插入时间标签
        res.put("updateDate", s);
//        System.out.println(date.toString());
//        System.out.println(res);
        return res;
    }

    //故障的字符串处理
    public Map<String, String> processFaultStr(HashMap<String, String> resTrainFault) {
        Map<String, String> faultLevel = new HashMap<>();
        Iterator<String> iterator = resTrainFault.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String s = resTrainFault.get(key);
            if (s.contains("重大故障")) {
                faultLevel.put(key, "fault");
            } else if (s.contains("中度故障")) {
                faultLevel.put(key, "fault");
            } else {
                faultLevel.put(key, "fault");
            }
        }
        return faultLevel;
    }


    //    4. 查询指定列车的信息
    public trainInfo getTianInfoByTrainNum(String lineNum, String trainNum) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        //运行模式、控制模式、当前站、下一站、信号强度、车速、列车总里程从MQ获取

//        目前还获取不到车总里程，这个自己构造
        Iterator<String> iterator = trainCardDao.getResTrainCard().keySet().iterator();
        int card_flag = 0;
        Map<String, String> card_map = new HashMap<>();
//        System.out.println(trainCardDao.getResTrainCard());
        String trainKey = getTrainKey(lineNum, trainNum);
//        System.out.println(trainKey);
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(trainKey)) {
                card_flag = 1;
                card_map = processKafkaRecordUtils.removeKeySpace(trainCardDao.getResTrainCard().get(key));
//                System.out.println(trainCardDao.getResTrainCard().get(key));
//                System.out.println(card_map);
                break;
            }
        }

//        System.out.println(card_map);

        //车辆状态(fault,warning,normal)从falut的MQ获取

        int fault_flag = 0;
//        Map<String, String> fault_map = processFaultStr(trainFaultDao.getResTrainFault());

        Map<String, String> fault_map = trainFaultDao.getFaultTrainKey();
        String fault_level = "normal";
//        System.out.println(trainKey);
        iterator = fault_map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
//            System.out.println(key);
            if (key.equals(trainKey)) {
                fault_flag = 1;
                fault_level = "fault";
            }
        }

        //是否在线：MQ里目前设置1分钟没有数据就认为是离线
        String is_online = "offline";
        int diff = 10;
        if (card_flag == 1) {   //在车辆卡片里存在，如果不存在一定是不在线
            java.util.Date date = new java.util.Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date saveDate = new Date();
            try {
                saveDate = sdf.parse(card_map.get("updateDate"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (saveDate.getYear() == date.getYear() && saveDate.getMonth() == date.getMonth() && date.getDay() == date.getDay() && date.getHours() == saveDate.getHours()) {
                int diffMin = saveDate.getMinutes() - date.getMinutes();
                diffMin = diffMin > 0 ? diffMin : -diffMin;
                if (diffMin < diff) {
                    is_online = "online";
                }
            }
        }

        //是否投运：从接口获取，目前没提供，全部设置为正常投运
        String is_operation = "yes";

        String network = "net01";
        String sign_strength = "";
        String run_model = "";
        String control_model = "";
        String current_station = "";
        String next_station = "";

        //列车总里程，这个在MQ里，目前未提供
        int total_train_length = 0;
        int speed = 0;
        if (card_flag != 0) {    //MQ里有相应的数据
//            System.out.println(card_map);
            sign_strength = card_map.get("sign_intensity");
            run_model = card_map.get("drive_model");
            control_model = card_map.get("control_model");
            current_station = card_map.get("current_station");
            next_station = card_map.get("next_station");
            speed = Integer.parseInt(card_map.get("trainspeed"));
            total_train_length=Integer.parseInt(card_map.get("totalmileage"));
        }
        return new trainInfo(Integer.parseInt(trainNum), Integer.parseInt(lineNum), network, sign_strength, run_model, control_model, current_station, next_station, fault_level, is_online, is_operation, total_train_length);
    }


    //查询所有列车的信息


    public Map<Integer, trainInfo> getAllTianInfoByLineNum(String lineNum) {
        Map<Integer, trainInfo> lineAllTrain = new HashMap<>();
        try {
            //最后需要查询的对象的key值
            //7001 表示7号线1号列车
            //7011表示7号线11号列车
            int train_num = getLineInfoById(lineNum).getTrain_num();
            for (int i = 0; i < train_num; ++i) {
                lineAllTrain.put(i, getTianInfoByTrainNum(lineNum, "" + i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineAllTrain;
    }

    public String getTrainKey(String lineNum, String trainNum) {
        String trainKey = "";
        if (lineNum.length() == 1) {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "00" + trainNum;
            } else {
                trainKey = lineNum + "0" + trainNum;
            }
        } else {
            if (trainNum.length() == 1) {
                trainKey = lineNum + "0" + trainNum;
            } else {
                trainKey = lineNum + trainNum;
            }
        }
        return trainKey;
    }
}
