package top.sheepyu.module.system.enums.config;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.system.constants.ErrorCodeConstants;

import java.util.Objects;

import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-21 15:00
 **/
@AllArgsConstructor
@Getter
public enum SystemConfigKeyEnum {
    CAPTCHA_ENABLE("captcha.enable", Boolean.class),
    //系统重置密码的默认密码
    DEFAULT_PASSWORD("password.default", String.class),
    //默认的消息发送实现
    DEFAULT_SMS_SENDER("sms-sender.default", String.class),
    //默认的文件上传实现
    DEFAULT_FILE_UPLOAD("file-upload.default", String.class),
    //默认文件分片大小(单位M)
    FILE_UPLOAD_PART_SIZE("file-upload.part-size", Integer.class),
    ;
    private final String key;
    private final Class<?> clazz;

    public static SystemConfigKeyEnum value(String key) {
        SystemConfigKeyEnum configKeyEnum = ArrayUtil.firstMatch(config -> Objects.equals(config.getKey(), key), values());
        if (configKeyEnum == null) {
            throw exception(ErrorCodeConstants.CONFIG_NOT_EXISTS);
        }
        return configKeyEnum;
    }
}
