package top.sheepyu.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ygq
 * @date 2022-12-02 21:23
 **/
@Data
public class ErrorCode implements Serializable {
    private final int code;
    private final String message;
}
