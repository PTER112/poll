package org.yjx.pollpojo.enums;

public enum StatusEnum {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用");
    private final int code;
    private final String desc;
    StatusEnum(int code, String desc) {
        this.code =code;
        this.desc = desc;
    }
    public int getCode(){return code;}
    public String getDesc(){return desc;}
}
