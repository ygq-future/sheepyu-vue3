package top.sheepyu.framework.mybatisplus.core.query;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import top.sheepyu.module.common.common.ErrorCode;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.CHECK_FIELD_NOT_NULL;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.OPERATION_FAILED;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.CollectionUtil.convertList;

/**
 * @author ygq
 * @date 2023-01-17 12:12
 * serviceImpl的增强
 **/
@Slf4j
public class ServiceImplX<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IServiceX<T> {
    @Override
    public PageResult<T> page(PageParam pageParam) {
        return page(pageParam, null);
    }

    @Override
    public PageResult<T> page(PageParam pageParam, LambdaQueryWrapper<T> wrapper) {
        Page<T> page = super.page(new Page<>(pageParam.getCurrent(), pageParam.getSize()), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean checkRepeatByField(T entity, SFunction<T, ?> field) {
        return checkRepeatByFields(entity, Collections.singletonList(field));
    }

    @Override
    public boolean checkRepeatByFields(T entity, Collection<SFunction<T, ?>> fields) {
        if (CollUtil.isEmpty(fields)) {
            throw exception(CHECK_FIELD_NOT_NULL);
        }

        LambdaQueryChainWrapper<T> query = lambdaQuery();
        for (SFunction<T, ?> field : fields) {
            Object apply = field.apply(entity);
            if (apply == null) {
                throw exception(CHECK_FIELD_NOT_NULL);
            }
            String fieldValue = apply.toString();
            query.eq(field, fieldValue);
        }

        List<T> list = query.list();
        boolean hasElement = CollUtil.isNotEmpty(list);
        if (hasElement && list.size() > 1) {
            log.error("表中已出现重复数据, 请检查!");
            log.error("重复数据: {}", list);
        }
        return hasElement;
    }

    @Override
    public void checkRepeatByFieldThrow(T entity, SFunction<T, ?> field, ErrorCode errorCode) {
        checkRepeatByFieldsThrow(entity, Collections.singletonList(field), errorCode);
    }

    @Override
    public void checkRepeatByFieldsThrow(T entity, Collection<SFunction<T, ?>> fields, ErrorCode errorCode) {
        boolean result = checkRepeatByFields(entity, fields);
        if (result) {
            throw exception(errorCode);
        }
    }

    @Override
    public T findByIdThrowIfNotExists(Object id, ErrorCode errorCode) {
        if (id == null) {
            throw exception(errorCode);
        }

        T t = getById(id.toString());
        if (t == null) {
            throw exception(errorCode);
        }
        return t;
    }

    @Override
    public T findByFieldThrowIfExists(SFunction<T, ?> field, Object fieldVal, ErrorCode errorCode) {
        T one = findByField(field, fieldVal);
        if (one != null) {
            throw exception(errorCode);
        }
        return one;
    }

    @Override
    public T findByField(SFunction<T, ?> field, Object fieldVal) {
        return lambdaQuery().eq(field, fieldVal).one();
    }

    @Override
    public <U1, U2> List<U1> findFieldValueByField(SFunction<T, U1> resField, SFunction<T, U2> byField, Collection<U2> byFieldValues) {
        List<T> list = lambdaQuery()
                .in(byField, byFieldValues)
                .select(resField)
                .list();
        return convertList(list, resField);
    }

    @Override
    public void batchDelete(String ids, SFunction<T, ?> fieldLambda) {
        batchDelete(ids, fieldLambda, buildQuery());
    }

    @Override
    public void batchDelete(Collection<? extends Serializable> idList, SFunction<T, ?> fieldLambda) {
        batchDelete(idList, fieldLambda, buildQuery());
    }

    @Override
    public void batchDelete(String ids, SFunction<T, ?> fieldLambda, LambdaQueryWrapperX<T> wrapperX) {
        List<String> idList = MyStrUtil.split(ids, ',');
        if (CollUtil.isEmpty(idList)) {
            return;
        }

        wrapperX.in(fieldLambda, idList);
        boolean result = remove(wrapperX);

        if (!result) {
            throw exception(OPERATION_FAILED);
        }
    }

    @Override
    public void batchDelete(Collection<? extends Serializable> idList, SFunction<T, ?> fieldLambda, LambdaQueryWrapperX<T> wrapperX) {
        wrapperX.in(fieldLambda, idList);
        boolean result = remove(wrapperX);

        if (!result) {
            throw exception(OPERATION_FAILED);
        }
    }

    @Override
    public LambdaQueryWrapperX<T> buildQuery() {
        return new LambdaQueryWrapperX<>();
    }
}
