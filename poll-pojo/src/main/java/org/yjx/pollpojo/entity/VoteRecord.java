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
public class VoteRecord {
    private Integer id;
    private Integer pollId;
    private Integer optionId;
    private Integer userId;
    private LocalDateTime voteTime;
}
