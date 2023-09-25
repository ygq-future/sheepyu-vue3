package top.sheepyu.framework.mybatisplus.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sheepyu.module.common.common.ErrorCode;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.common.PageResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-18 17:33
 **/
public interface IServiceX<T> extends IService<T> {
    PageResult<T> page(PageParam pageParam);

    PageResult<T> page(PageParam pageParam, LambdaQueryWrapper<T> wrapper);

    /**
     * 检查单字段在表中是否已经存在
     *
     * @param entity 实体类
     * @param field  字段表达式
     * @return result
     */
    boolean checkRepeatByField(T entity, SFunction<T, ?> field);

    /**
     * 检查多字段在表中是否已经存在
     *
     * @param entity 实体类
     * @param fields 字段表达式
     * @return result
     */
    boolean checkRepeatByFields(T entity, Collection<SFunction<T, ?>> fields);

    /**
     * 检查单字段在表中是否已经存在, 但是会抛出自定义异常
     *
     * @param entity    实体类
     * @param field     字段表达式
     * @param errorCode 错误码
     */
    void checkRepeatByFieldThrow(T entity, SFunction<T, ?> field, ErrorCode errorCode);

    /**
     * 检查多字段在表中是否已经存在, 但是会抛出自定义异常
     *
     * @param entity    实体类
     * @param fields    字段表达式
     * @param errorCode 错误码
     */
    void checkRepeatByFieldsThrow(T entity, Collection<SFunction<T, ?>> fields, ErrorCode errorCode);

    /**
     * 根据主键查找数据, 如果找不到就会报错
     *
     * @param id        字段
     * @param errorCode 报错的错误码
     * @return 返回单条数据
     */
    T findByIdThrowIfNotExists(Object id, ErrorCode errorCode);

    /**
     * 根据某个字段查找数据, 如果找不到就会报错
     *
     * @param field     字段
     * @param fieldVal  要查找的字段值
     * @param errorCode 报错的错误码
     * @return 返回单条数据
     */
    T findByFieldThrowIfExists(SFunction<T, ?> field, Object fieldVal, ErrorCode errorCode);

    /**
     * 根据某个字段查找数据, 找不到不会报错
     *
     * @param field    字段
     * @param fieldVal 要查找的字段值
     * @return 返回单条数据
     */
    T findByField(SFunction<T, ?> field, Object fieldVal);

    /**
     * @param ids         要删除的资源的唯一标识
     * @param fieldLambda 要根据哪个字段进行删除的表达式
     */
    void batchDelete(String ids, SFunction<T, ?> fieldLambda);

    /**
     * @param idList      要删除的资源的唯一标识列表
     * @param fieldLambda 要根据哪个字段进行删除的表达式
     */
    void batchDelete(List<? extends Serializable> idList, SFunction<T, ?> fieldLambda);

    /**
     * @param ids         要删除的资源的唯一标识
     * @param fieldLambda 要根据哪个字段进行删除的表达式
     * @param wrapperX    删除需要什么条件, 注意这里就不要加需要删除的字段了
     */
    void batchDelete(String ids, SFunction<T, ?> fieldLambda, LambdaQueryWrapperX<T> wrapperX);

    /**
     * @param idList      要删除的资源的唯一标识列表
     * @param fieldLambda 要根据哪个字段进行删除的表达式
     * @param wrapperX    删除需要什么条件, 注意这里就不要加需要删除的字段了
     */
    void batchDelete(List<? extends Serializable> idList, SFunction<T, ?> fieldLambda, LambdaQueryWrapperX<T> wrapperX);

    /**
     * 构建自定义增强条件
     *
     * @return LambdaQueryWrapperX
     */
    LambdaQueryWrapperX<T> buildQuery();
}
