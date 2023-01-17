package top.sheepyu.framework.mybatisplus.core.query;

import cn.hutool.core.collection.CollUtil;
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

import java.util.Collection;
import java.util.List;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.CHECK_FIELD_NOT_NULL;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.OPERATION_FAILED;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-17 12:12
 * serviceImpl的增强
 **/
@Slf4j
public class ServiceImplX<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    public PageResult<T> page(PageParam pageParam) {
        return page(pageParam, null);
    }

    public PageResult<T> page(PageParam pageParam, LambdaQueryWrapperX<T> wrapperX) {
        Page<T> page = super.page(new Page<>(pageParam.getCurrent(), pageParam.getSize()), wrapperX);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    /**
     * 检查多字段在表中是否已经存在, 但是会抛出自定义异常
     *
     * @param entity    实体类
     * @param fields    字段表达式
     * @param errorCode 错误码
     */
    public void checkRepeatByFieldThrow(T entity, ErrorCode errorCode, Collection<SFunction<T, ?>> fields) {
        boolean result = checkRepeatByField(entity, fields);
        if (result) {
            throw exception(errorCode);
        }
    }

    /**
     * 检查多字段在表中是否已经存在
     *
     * @param entity 实体类
     * @param fields 字段表达式
     * @return result
     */
    public final boolean checkRepeatByField(T entity, Collection<SFunction<T, ?>> fields) {
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

    public T findByIdValidateExists(Object id, ErrorCode errorCode) {
        T t = getById(id.toString());
        if (t == null) {
            throw exception(errorCode);
        }
        return t;
    }

    public void batchDelete(String ids, SFunction<T, ?> fieldLambda) {
        batchDelete(ids, fieldLambda, buildQuery());
    }

    /**
     * @param ids         要删除的资源的唯一标识
     * @param fieldLambda 要根据哪个字段进行删除的表达式
     * @param wrapperX    删除需要什么条件, 注意这里就不要加需要删除的字段了
     */
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

    public LambdaQueryWrapperX<T> buildQuery() {
        return new LambdaQueryWrapperX<>();
    }
}
