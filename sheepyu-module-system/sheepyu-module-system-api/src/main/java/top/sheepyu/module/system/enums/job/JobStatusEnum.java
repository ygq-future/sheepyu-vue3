package top.sheepyu.module.system.enums.job;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.STATUS_ERROR;

/**
 * 任务状态的枚举
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum implements IterableEnum {
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

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(JobStatusEnum::getCode).collect(Collectors.toList());
    }
}
