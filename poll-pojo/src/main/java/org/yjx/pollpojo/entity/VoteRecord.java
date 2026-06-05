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
    private int id;
    private int pollId;
    private int optionId;
    private int userId;
    private LocalDateTime createTime;
}
