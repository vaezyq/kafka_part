package com.example.kafka_test.controller;
import com.example.kafka_test.dto.MyResponseBody;
import com.example.kafka_test.dto.video.LineVideoConfigDto;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import com.example.kafka_test.service.TrainVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(value = "/video")
public class TrainVideoController {

    @Autowired
    private TrainVideoService trainVideoService;

    @GetMapping(value = "/lineClassify")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据父节点得到分类信息")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
/**
 * Description: 
 * date: 2022/10/11 22:03
 * @author: Haodong Li
 * @since JDK 1.8
 * @param: query 
 * @return java.lang.Object
*/
    public Object getClassifyByParentId(LineClassifyQuery query){
        try {
            Object res =  trainVideoService.getClassifyByParentId(query);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }

    }

    /**
     * Description: 根据四级下拉选项以及位置搜索视频地址
     * date: 2022/10/12 15:02
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    @GetMapping(value = "/getLineVideoConfig")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据四级下拉选项以及位置搜索视频地址")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
    public Object getLineVideoConfig(@RequestParam("id1") Integer id1,@RequestParam("id2") Integer id2
    ,@RequestParam("id3") Integer id3,@RequestParam("id4") Integer id4,@RequestParam("place") String  place){
        try {
            Object res =  trainVideoService.getLineVideoConfig(id1,id2,id3,id4,place);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }

    }

    /**
     * Description: 批量插入视频配置信息
     * date: 2022/10/12 15:17
     * @author: Haodong Li
     * @since: JDK 1.8
      
    */
    @PostMapping(value = "/insertVideoConfig")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据四级下拉选项以及位置搜索视频地址")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
    public Object insertVideoConfig(@RequestBody List<LineVideoConfigDto> dtoList){
        try {
            Object res =  trainVideoService.insertLineVideoConfig(dtoList);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    /**
     * Description: 批量更新视频配置信息
     * date: 2022/10/12 15:17
     * @author: Haodong Li
     * @since: JDK 1.8

     */
    @PostMapping(value = "/updateVideoConfig")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据四级下拉选项以及位置搜索视频地址")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
    public Object updateVideoConfig(@RequestBody List<LineVideoConfigDto> dtoList){
        try {
            Object res =  trainVideoService.updateLineVideoConfig(dtoList);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }

    /**
     * Description: 批量删除视频配置信息
     * date: 2022/10/12 15:17
     * @author: Haodong Li
     * @since: JDK 1.8

     */
    @PostMapping(value = "/deleteVideoConfig")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "根据四级下拉选项以及位置搜索视频地址")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = MyResponseBody.class)})
    public Object deleteVideoConfig(@RequestBody List<Integer> dtoList){
        try {
            Object res =  trainVideoService.deleteLineVideoConfig(dtoList);
            return new MyResponseBody("200", "success", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseBody("400", "fail", "");
        }
    }
}
