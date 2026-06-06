package org.yjx.pollpojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
       private Integer id;
       private String title;
       private String description;
       private Integer creatorId;
       private LocalDateTime createTime;
}
