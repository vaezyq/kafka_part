package com.example.kafka_test.pojo.query;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haodong Li
 * @date 2022年10月12日 19:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineVideoConfigUpdate {
    private Integer id;
    private Integer line_classify_level1_id;
    private Integer line_classify_level2_id;
    private Integer line_classify_level3_id;
    private Integer line_classify_level4_id;
    private String video_url;
    private Integer index;
    private Integer place;
}
