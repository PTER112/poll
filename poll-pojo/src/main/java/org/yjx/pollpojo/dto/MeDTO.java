package org.yjx.pollpojo.dto;

import lombok.Data;

@Data
public class MeDTO {
    private Integer id;
    private String username;
    private String oldpassword;
    private String newpassword;
}