package org.yjx.pollpojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollOption {
    private Integer id;
    private String optionText;
    private Integer pollId;
    private Integer voteCount;
}
