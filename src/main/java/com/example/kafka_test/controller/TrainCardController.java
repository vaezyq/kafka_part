package com.example.kafka_test.controller;

import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.dto.trainInfo;
import com.example.kafka_test.service.TrainCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengli
 * @ClassName: LineInfoController
 * @Description: 线路信息
 * @date 2022年2月4日 下午3:13:41
 * city	project	lineNum	insert_time	collect_time	base	opening_time	power	station_num	total_length	transfer_num
 * 城市	项目	线路号	数据入库时间	配置数据字段时间	基地	开通时间	供电方式	车站数	线路总长度	可换乘线路数
 * trainNum	lineNum	network	signal_strength	model	current_station	next_station	status	is_online	is_operation	insert_time	collect_time
 * 车辆编号	线路号	车地网络	信号强度	运行模式	当前站	下一站	列车状态	是否在线	是否投运	数据入库时间	配置数据字段时间
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/lineInfoCtl")
public class TrainCardController {


    @Autowired
    TrainCardService trainCardService;


    //1 查询某条线路信息   finished
    @GetMapping(value = "/line")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getLineInfoById(@RequestParam("lineNum") String lineNum) {
//        查询线路信息
        try {

            return new MyResponseBody("200", "success", trainCardService.getLineInfoById(lineNum));
//            return trainCardService.getLineInfoById(lineNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    //2 所有线路信息   finished
//    @GetMapping(value = "/line/{getAll}")
    @GetMapping(value = "/line/getAll")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getLineInfos() {
        try {
            return new MyResponseBody("200", "success", trainCardService.getLineInfos());
//            return trainCardService.getLineInfos();
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    //3查询当前线路所有车辆信息   finished
    //    @ApiImplicitParam(name = "lineNum", value = "lineNum", required = true, dataType = "String")
    @GetMapping(value = "/trainInfos")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfos(@RequestParam("lineNum") String lineNum) {
        try {
            return new MyResponseBody("200", "sucess", trainCardService.getAllTianInfoByLineNum(lineNum));
//            return trainCardService.getAllTianInfoByLineNum(lineNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }


    //4查询当前线路下一条车辆信息   finished
    @GetMapping(value = "/trainInfo")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoByTrainNum(@RequestParam("lineNum") String lineNum, @RequestParam("trainNum") String trainNum) {
        try {
            return new MyResponseBody("200", "sucess", trainCardService.getTianInfoByTrainNum(lineNum, trainNum));
//            return trainCardService.getTianInfoByTrainNum(lineNum, trainNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }


    //5查询所有投运状态的列车 is_online   finished
    @GetMapping(value = "/trainInfo/operation")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoOperation(@RequestParam("lineNum") String lineNum) {
        try {
            Map<Integer, trainInfo> operationLineNumTrainInfo = new HashMap<>();
            Map<Integer, trainInfo> allLineNumTrainInfo = trainCardService.getAllTianInfoByLineNum(lineNum);

            for (Map.Entry<Integer, trainInfo> entry : allLineNumTrainInfo.entrySet()) {

                //这个投运字段的结果还没有给定,目前全部为投运
                if (entry.getValue().getIs_operation().equals("yes")) {
                    operationLineNumTrainInfo.put(entry.getKey(), entry.getValue());
                }
            }
            return new MyResponseBody("200", "success", operationLineNumTrainInfo);
//            return operationLineNumTrainInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }

    }

    //6查询所有在线状态的列车   finished
    @GetMapping(value = "/trainInfo/online")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoOnline(@RequestParam("lineNum") String lineNum) {
        try {
            Map<Integer, trainInfo> onlineLineNumTrainInfo = new HashMap<>();
            Map<Integer, trainInfo> allLineNumTrainInfo = trainCardService.getAllTianInfoByLineNum(lineNum);
            for (Map.Entry<Integer, trainInfo> entry : allLineNumTrainInfo.entrySet()) {
                if (entry.getValue().getIs_online().equals("online")) {
                    onlineLineNumTrainInfo.put(entry.getKey(), entry.getValue());
                }
            }
            return new MyResponseBody("200", "success", onlineLineNumTrainInfo);
//            return onlineLineNumTrainInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }


    //7查询所有离线状态的列车
    @GetMapping(value = "/trainInfo/outline")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoOutline(@RequestParam("lineNum") String lineNum) {
        try {
            Map<Integer, trainInfo> offlineLineNumTrainInfo = new HashMap<>();
            Map<Integer, trainInfo> allLineNumTrainInfo = trainCardService.getAllTianInfoByLineNum(lineNum);
            for (Map.Entry<Integer, trainInfo> entry : allLineNumTrainInfo.entrySet()) {
                if (entry.getValue().getIs_online().equals("offline")) {
                    offlineLineNumTrainInfo.put(entry.getKey(), entry.getValue());
                }
            }
            return new MyResponseBody("200", "success", offlineLineNumTrainInfo);
//            return offlineLineNumTrainInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    //01 正常 fault 故障 warning 预警
    //8查询所有故障状态的列车,目前fault里的全部为故障，预警数据还没有
    @GetMapping(value = "/trainInfo/status02")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoStatus02(@RequestParam("lineNum") String lineNum) {
        try {
            Map<Integer, trainInfo> offlineLineNumTrainInfo = new HashMap<>();
            Map<Integer, trainInfo> allLineNumTrainInfo = trainCardService.getAllTianInfoByLineNum(lineNum);
            for (Map.Entry<Integer, trainInfo> entry : allLineNumTrainInfo.entrySet()) {
                if (entry.getValue().getStatus().equals("fault")) {
                    offlineLineNumTrainInfo.put(entry.getKey(), entry.getValue());
                }
            }
            return new MyResponseBody("200", "success", "");
//            return offlineLineNumTrainInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    //9查询所有预警状态的列车
    @GetMapping(value = "/trainInfo/status03")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Object getTianInfoStatus03(@RequestParam("lineNum") String lineNum) {
//        try {
//            Map<Integer, trainInfo> offlineLineNumTrainInfo = new HashMap<>();
//            Map<Integer, trainInfo> allLineNumTrainInfo = trainCardService.getAllTianInfoByLineNum(lineNum);
//            for (Map.Entry<Integer, trainInfo> entry : allLineNumTrainInfo.entrySet()) {
//                if (entry.getValue().getStatus().equals("warning")) {
//                    offlineLineNumTrainInfo.put(entry.getKey(), entry.getValue());
//                }
//            }
//            return offlineLineNumTrainInfo;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
        return new MyResponseBody("400", "fail", ""); //目前还没有健康预警数据
//        return "";
    }


}
