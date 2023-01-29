package top.sheepyu.framework.file.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ygq
 * @date 2023-01-29 17:04
 **/
@AllArgsConstructor
@Getter
public enum FileUploadCompleteEnum {
    COMPLETE(1), //完成
    INCOMPLETE(0), //未完成
    ;
    private final int code;
}
