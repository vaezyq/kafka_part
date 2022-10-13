package com.example.kafka_test.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haodong Li
 * @date 2022年10月12日 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineVideoConfigInsertDto {
    private Integer line_classify_level1_id;
    private Integer line_classify_level2_id;
    private Integer line_classify_level3_id;
    private Integer line_classify_level4_id;
    private String video_url;
    private Integer index;
    private Integer place;
}
