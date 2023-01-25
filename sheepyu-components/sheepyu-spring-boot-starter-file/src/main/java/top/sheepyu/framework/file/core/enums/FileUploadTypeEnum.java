package top.sheepyu.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.FILE_UPLOAD_DONT_MATCH;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-25 15:09
 **/
@AllArgsConstructor
@Getter
public enum FileUploadTypeEnum {
    LOCAL("LOCAL"), //本地存储
    ALIYUN("ALIYUN"), //阿里云oss
    QCLOUD("QCLOUD") //腾讯云cos
    ;
    private final String code;

    public static FileUploadTypeEnum value(String code) {
        FileUploadTypeEnum typeEnum = ArrayUtil.firstMatch(type -> code.equalsIgnoreCase(type.getCode()), values());
        if (typeEnum == null) {
            throw exception(FILE_UPLOAD_DONT_MATCH);
        }
        return typeEnum;
    }
}
