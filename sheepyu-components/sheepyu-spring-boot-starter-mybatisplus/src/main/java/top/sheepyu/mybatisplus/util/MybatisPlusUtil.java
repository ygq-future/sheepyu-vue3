package top.sheepyu.mybatisplus.util;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static top.sheepyu.common.constants.ErrorCodeConstants.EXISTS;
import static top.sheepyu.common.constants.ErrorCodeConstants.NOT_FOUND;
import static top.sheepyu.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-15 11:49
 **/
@Slf4j
public class MybatisPlusUtil {
    public static <T, ID> T checkNotFound(ID id, BaseMapper<T> baseMapper) {
        T t = baseMapper.selectById(id.toString());
        if (t == null) {
            throw exception(NOT_FOUND);
        }
        return t;
    }

    public static <T> void checkRepeatByFieldThrow(T t, SFunction<T, ?> fun, BaseMapper<T> baseMapper) {
        boolean result = checkRepeatByField(t, fun, baseMapper);
        if (result) {
            throw exception(EXISTS);
        }
    }

    public static <T> boolean checkRepeatByField(T t, SFunction<T, ?> fun, BaseMapper<T> baseMapper) {
        String fieldValue = fun.apply(t).toString();
        List<T> list = new LambdaQueryChainWrapper<>(baseMapper).eq(fun, fieldValue).list();
        boolean hasElement = CollUtil.isNotEmpty(list);
        if (hasElement && list.size() > 1) {
            log.error("表中已出现重复数据, 请检查!");
            log.error("重复数据: {}", list);
        }
        return hasElement;
    }
}
