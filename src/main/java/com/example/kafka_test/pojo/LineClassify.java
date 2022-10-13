package com.example.kafka_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineClassify {
    private Integer id;
    private Integer parent_id;
    private Integer level;
    private String name;
    private Integer index;
    private Boolean del;
    private Date create_time;
    private Date update_time;
    private String create_source;
    private String update_source;

}
