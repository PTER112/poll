package org.yjx.pollpojo.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;//错误信息的报错
    private T data;//数据

   public static<T> Result<T> success(){
       Result<T> result = new Result<>();
       result.code = 200;
       result.msg = "success";
       return result;
   }
   public static<T> Result<T> success(T data){
       Result<T> result=new Result<>();
       result.code=200;
       result.msg="success";
       result.data=data;
       return result;
   }
   public static<T> Result<T> error(Integer code,String msg){
       Result<T> result=new Result<>();
       result.code=code;
       result.msg=msg;
       return result;
   }
}
