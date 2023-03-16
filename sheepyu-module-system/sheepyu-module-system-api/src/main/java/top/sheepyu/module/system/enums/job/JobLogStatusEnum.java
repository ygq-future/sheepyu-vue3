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
public enum JobLogStatusEnum implements IterableEnum {
    SUCCESS(1), //成功
    FAILED(0), //失败
    ;
    private final int code;

    public static JobLogStatusEnum valueOf(int code) {
        JobLogStatusEnum status = ArrayUtil.firstMatch(e -> e.getCode() == code, values());
        if (status == null) {
            throw exception(STATUS_ERROR);
        }
        return status;
    }

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(JobLogStatusEnum::getCode).collect(Collectors.toList());
    }
}
