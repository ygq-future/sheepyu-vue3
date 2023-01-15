package top.sheepyu.common.exception;

import lombok.Getter;
import top.sheepyu.common.common.ErrorCode;

/**
 * @author ygq
 * @date 2022-12-02 21:46
 **/
@Getter
public class CommonException extends RuntimeException {
    private final int code;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public static CommonException exception(ErrorCode errorCode) {
        return new CommonException(errorCode);
    }
}
