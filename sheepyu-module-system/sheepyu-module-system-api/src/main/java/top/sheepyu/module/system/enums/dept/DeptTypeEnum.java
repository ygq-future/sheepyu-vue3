package top.sheepyu.module.system.enums.dept;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DEPT_TYPE_NOT_EXISTS;

/**
 * @author ygq
 * @date 2023-01-30 11:09
 **/
@AllArgsConstructor
@Getter
public enum DeptTypeEnum implements IterableEnum {
    DEPT(0),    //部门
    POST(1),    //职位
    USER(2),    //用户
    ROLE(3),    //角色
    ;
    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(DeptTypeEnum::getCode).collect(Collectors.toList());
    }

    public static DeptTypeEnum value(int code) {
        DeptTypeEnum deptTypeEnum = ArrayUtil.firstMatch(dept -> Objects.equals(dept.getCode(), code), values());
        if (deptTypeEnum == null) {
            throw exception(DEPT_TYPE_NOT_EXISTS);
        }
        return deptTypeEnum;
    }
}
