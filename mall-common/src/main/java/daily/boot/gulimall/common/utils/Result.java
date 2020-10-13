package daily.boot.gulimall.common.utils;

import daily.boot.gulimall.common.exception.BizCodeEnum;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6753531128464673746L;
    
    private Integer code; //返回码
    private String msg; //返回消息
    private T data; //返回数据
    
    private Result(T data) {
        this(data, BizCodeEnum.SUCCESS.getMsg());
    }
    
    private Result(T data, String msg) {
        this.code = BizCodeEnum.SUCCESS.getCode();
        this.msg = msg;
        this.data = data;
    }
    
    private Result(){}
    
    public static Result ok() {
        Result result = new Result();
        result.code = BizCodeEnum.SUCCESS.getCode();
        result.msg = BizCodeEnum.SUCCESS.getMsg();
        return result;
    }
    
    public static Result ok(String msg) {
        Result result = new Result();
        result.code = BizCodeEnum.SUCCESS.getCode();
        result.msg = msg;
        return result;
    }
    
    public static Result error() {
        Result result = new Result();
        result.code = BizCodeEnum.UNKNOW_EXCEPTION.getCode();
        result.msg = BizCodeEnum.UNKNOW_EXCEPTION.getMsg();
        return result;
    }
    
    public static Result error(String msg) {
        Result result = new Result();
        result.code = BizCodeEnum.UNKNOW_EXCEPTION.getCode();
        result.msg = msg;
        return result;
    }
    
    public static Result error(BizCodeEnum bizCodeEnum) {
        Result result = new Result();
        result.code = bizCodeEnum.getCode();
        result.msg = bizCodeEnum.getMsg();
        return result;
    }
    
    public static <E> Result<E> ok(E data) {
        return new Result<>(data);
    }
    public static <E> Result<E> ok(E data, String message) {
        return new Result<>(data, message);
    }
    
}
