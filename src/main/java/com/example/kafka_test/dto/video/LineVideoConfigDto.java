package com.example.kafka_test.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haodong Li
 * @date 2022年10月12日 15:43
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineVideoConfigDto {
    private Integer id;
    private Integer lineClassifyLevel1Id;
    private Integer lineClassifyLevel2Id;
    private Integer lineClassifyLevel3Id;
    private Integer lineClassifyLevel4Id;
    private String videoUrl;
    private Integer index;
    private Integer place;
}
