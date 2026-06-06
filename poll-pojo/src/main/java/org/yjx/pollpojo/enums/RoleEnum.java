package org.yjx.pollpojo.enums;

public enum RoleEnum {
    USER("user","用户"),//注意这里不是分号，是逗号
    ADMIN("admin","管理员");
    private final String code;
    private final String desc;

    RoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode(){return code;}
    public String getDesc(){return desc;}
}
