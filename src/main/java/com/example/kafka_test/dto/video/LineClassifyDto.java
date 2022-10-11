package com.example.kafka_test.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineClassifyDto {
    private Integer id;
    private Integer parent_id;
    private Integer level;
    private String name;
    private Integer index;
}
