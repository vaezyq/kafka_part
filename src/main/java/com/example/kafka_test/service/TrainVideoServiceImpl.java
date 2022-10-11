package com.example.kafka_test.service;

import com.example.kafka_test.dao.LineClassifyDao;
import com.example.kafka_test.dto.video.LineClassifyDto;
import com.example.kafka_test.pojo.LineClassify;
import com.example.kafka_test.pojo.query.LineClassifyQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
}
