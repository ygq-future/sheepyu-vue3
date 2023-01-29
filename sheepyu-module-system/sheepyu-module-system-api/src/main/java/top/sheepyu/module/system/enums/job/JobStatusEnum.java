package top.sheepyu.module.system.enums.job;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.STATUS_ERROR;

/**
 * 任务状态的枚举
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    INIT(0), //初始化
    NORMAL(1), //已开启
    STOP(2), //已暂停
    ;

    private final int code;

    public static JobStatusEnum valueOf(int code) {
        JobStatusEnum status = ArrayUtil.firstMatch(e -> e.getCode() == code, values());
        if (status == null) {
            throw exception(STATUS_ERROR);
        }
        return status;
    }
}
