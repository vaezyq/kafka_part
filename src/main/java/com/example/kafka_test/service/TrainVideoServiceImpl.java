package com.example.kafka_test.service;

import com.example.kafka_test.dao.LineClassifyDao;
import com.example.kafka_test.dao.LineVideoConfigDao;
import com.example.kafka_test.dto.video.LineClassifyDto;
import com.example.kafka_test.dto.video.LineVideoConfigDto;
import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.LineVideoConfig;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import com.example.kafka_test.pojo.query.LineVideoConfigUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.dozer.DozerBeanMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrainVideoServiceImpl implements  TrainVideoService{
    @Autowired
    private LineClassifyDao lineClassifyDao;

    @Autowired
    private LineVideoConfigDao lineVideoConfigDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override

    /**
     * Description: 根据父节点ID获取下拉分类
     * dozer映射
     * date: 2022/10/11 22:47
     * @author: Haodong Li
     * @since: JDK 1.8
     * @param: query
     * @return java.util.List<com.example.kafka_test.dto.video.LineClassifyDto>
    */
    public List<LineClassifyDto> getClassifyByParentId(LineClassifyQuery query) {

        List<LineClassifyDto> res = new ArrayList<>();
        List<LineClassify>  entityList =  lineClassifyDao.classifyByParentId(query);
        for (LineClassify item:entityList){
            LineClassifyDto dto = new DozerBeanMapper().map(item, LineClassifyDto.class);
            res.add(dto);
        }
        return res;
    }

    /**
     * Description:
     * date: 2022/10/12 14:37
     * @author: Haodong Li
     * @since: JDK 1.8
      入参为四级分类的Id
     * @return
    */
    public List<LineVideoConfig> getLineVideoConfig(Integer id1, Integer id2, Integer id3, Integer id4,String place){
        List<LineVideoConfig> res;
        String filterStr = " where del = 0" + (id1!=0?" and line_classify_level1_id = "+id1.toString():"")
                + (id2!=0?" and line_classify_level2_id = "+id2.toString():"")
                + (id3!=0?" and line_classify_level3_id = "+id3.toString():"")
                + (id4!=0?" and line_classify_level4_id = "+id4.toString():"")
                + (!place.isEmpty()?" and place = "+place:"");;
        res = jdbcTemplate.query("select * from line_video_config"+filterStr,new BeanPropertyRowMapper<LineVideoConfig>(LineVideoConfig.class));
        return  res;
    }

    /**
     * Description: 插入数据并返回对应的列表
     * date: 2022/10/12 15:44
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public List<LineVideoConfigDto> insertLineVideoConfig(List<LineVideoConfigDto> list){
        for (LineVideoConfigDto item:list) {
            LineVideoConfig data = new DozerBeanMapper().map(item, LineVideoConfig.class);
            data.setCreateSource("LineVideoController");
            lineVideoConfigDao.insertSelective(data);
            //item.setId(id);
        }
        return  list;
    }

    /**
     * Description: 更新
     * date: 2022/10/12 20:34
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public List<LineVideoConfigDto> updateLineVideoConfig(List<LineVideoConfigDto> list){
        for (LineVideoConfigDto item:list) {
            LineVideoConfig data = new DozerBeanMapper().map(item, LineVideoConfig.class);
            data.setUpdateSource("LineVideoController");
            data.setDel(false);
           lineVideoConfigDao.updateByPrimaryKey(data);
            //item.setId(id);
        }
        return  list;
    }
    /**
     * Description: 删除
     * date: 2022/10/12 20:34
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public Integer deleteLineVideoConfig(List<Integer> list){
        Integer num = 0;
        for (Integer id:list) {
            num+=lineVideoConfigDao.deleteByPrimaryKey(id);
        }
        return  num;
    }
}
