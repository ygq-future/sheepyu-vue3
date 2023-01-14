package top.sheepyu.common;

import lombok.Data;

import java.util.Date;

@Data
public class Result<T> {
    private int code;
    private String message;
    private Date time;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = new Date();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功!", data);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
