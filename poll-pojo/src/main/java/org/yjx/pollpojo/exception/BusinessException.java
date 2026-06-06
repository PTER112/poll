package org.yjx.pollpojo.exception;

public class BusinessException extends RuntimeException {
  public final int code;
  public BusinessException(String msg,int code) {
    super(msg);
    this.code = code;
  }
  public int getCode(){return code;}
}
