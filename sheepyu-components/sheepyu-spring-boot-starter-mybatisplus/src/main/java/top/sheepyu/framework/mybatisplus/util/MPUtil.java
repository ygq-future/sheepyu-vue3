package top.sheepyu.framework.mybatisplus.util;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import top.sheepyu.module.common.common.ErrorCode;
import top.sheepyu.module.common.util.MyStrUtil;

import java.util.List;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-15 11:49
 **/
@Slf4j
public class MPUtil {
    public static <T, ID> T findByIdValidateExists(ID id, BaseMapper<T> baseMapper) {
        return findByIdValidateExists(id, baseMapper, NOT_FOUND);
    }

    public static <T, ID> T findByIdValidateExists(ID id, BaseMapper<T> baseMapper, ErrorCode errorCode) {
        T t = baseMapper.selectById(id.toString());
        if (t == null) {
            throw exception(errorCode);
        }
        return t;
    }

    public static <T> void batchDelete(String ids, SFunction<T, ?> fieldLambda, BaseMapper<T> baseMapper) {
        List<String> idList = MyStrUtil.split(ids, ',');
        if (CollUtil.isEmpty(idList)) {
            return;
        }

        boolean result = new LambdaUpdateChainWrapper<>(baseMapper).in(fieldLambda).remove();
        if (!result) {
            throw exception(OPERATION_FAILED);
        }
    }

    public static <T> void checkRepeatByFieldThrow(T entity, SFunction<T, ?> fieldLambda, BaseMapper<T> baseMapper) {
        checkRepeatByFieldThrow(entity, fieldLambda, baseMapper, EXISTS);
    }

    public static <T> void checkRepeatByFieldThrow(T entity, SFunction<T, ?> fieldLambda, BaseMapper<T> baseMapper, ErrorCode errorCode) {
        boolean result = checkRepeatByField(entity, fieldLambda, baseMapper);
        if (result) {
            throw exception(errorCode);
        }
    }

    public static <T> boolean checkRepeatByField(T entity, SFunction<T, ?> fieldLambda, BaseMapper<T> baseMapper) {
        String fieldValue = fieldLambda.apply(entity).toString();
        List<T> list = new LambdaQueryChainWrapper<>(baseMapper).eq(fieldLambda, fieldValue).list();
        boolean hasElement = CollUtil.isNotEmpty(list);
        if (hasElement && list.size() > 1) {
            log.error("表中已出现重复数据, 请检查!");
            log.error("重复数据: {}", list);
        }
        return hasElement;
    }
}
