package org.yjx.pollpojo.enums;

public enum StatusEnum {
    DISABLED(1,"禁用"),
    ENABLED(0,"启用");
    private final int code;
    private final String desc;
    StatusEnum(int code, String desc) {
        this.code =code;
        this.desc = desc;
    }
    public int getCode(){return code;}
    public String getDesc(){return desc;}
}
