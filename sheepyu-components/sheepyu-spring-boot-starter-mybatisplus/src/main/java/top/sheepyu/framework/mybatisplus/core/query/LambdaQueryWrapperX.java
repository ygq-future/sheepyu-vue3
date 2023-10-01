package top.sheepyu.framework.mybatisplus.core.query;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @param <T> 数据类型
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

    public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String val) {
        return likeIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> likeIfPresent(boolean condition, SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return like(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> like(SFunction<T, ?> column, Object val) {
        super.like(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> like(boolean condition, SFunction<T, ?> column, Object val) {
        super.like(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return inIfPresent(true, column, values);
    }

    public LambdaQueryWrapperX<T> inIfPresent(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return in(condition, column, values);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Collection<?> values) {
        super.in(column, values);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> in(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        super.in(condition, column, values);
        return this;
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        return inIfPresent(true, column, values);
    }

    public LambdaQueryWrapperX<T> inIfPresent(boolean condition, SFunction<T, ?> column, Object... values) {
        if (!ArrayUtil.isEmpty(values)) {
            return in(condition, column, values);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> in(SFunction<T, ?> column, Object... values) {
        super.in(column, values);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> in(boolean condition, SFunction<T, ?> column, Object... values) {
        super.in(condition, column, values);
        return this;
    }

    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        return eqIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> eqIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return eq(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        return neIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> neIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return ne(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> ne(SFunction<T, ?> column, Object val) {
        super.ne(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> ne(boolean condition, SFunction<T, ?> column, Object val) {
        super.ne(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        return gtIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> gtIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return gt(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> gt(SFunction<T, ?> column, Object val) {
        super.gt(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> gt(boolean condition, SFunction<T, ?> column, Object val) {
        super.gt(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        return geIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> geIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return ge(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> ge(SFunction<T, ?> column, Object val) {
        super.ge(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> ge(boolean condition, SFunction<T, ?> column, Object val) {
        super.ge(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        return ltIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> ltIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return lt(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> lt(SFunction<T, ?> column, Object val) {
        super.lt(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> lt(boolean condition, SFunction<T, ?> column, Object val) {
        super.lt(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        return leIfPresent(true, column, val);
    }

    public LambdaQueryWrapperX<T> leIfPresent(boolean condition, SFunction<T, ?> column, Object val) {
        if (val != null && StrUtil.isNotBlank(val.toString())) {
            return le(condition, column, val);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> le(SFunction<T, ?> column, Object val) {
        super.lt(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> le(boolean condition, SFunction<T, ?> column, Object val) {
        super.lt(condition, column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        return betweenIfPresent(true, column, val1, val2);
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return between(condition, column, val1, val2);
        }
        if (val1 != null) {
            return ge(condition, column, val1);
        }
        if (val2 != null) {
            return le(condition, column, val2);
        }
        return this;
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        return betweenIfPresent(true, column, values);
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(boolean condition, SFunction<T, ?> column, Object[] values) {
        Object val1 = ArrayUtils.get(values, 0);
        Object val2 = ArrayUtils.get(values, 1);
        return betweenIfPresent(condition, column, val1, val2);
    }

    @Override
    public LambdaQueryWrapperX<T> between(SFunction<T, ?> column, Object val1, Object val2) {
        super.between(column, val1, val2);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> between(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        super.between(condition, column, val1, val2);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> or(boolean condition, Consumer<LambdaQueryWrapper<T>> consumer) {
        super.or(condition, consumer);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> or(Consumer<LambdaQueryWrapper<T>> consumer) {
        super.or(consumer);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> or() {
        super.or();
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> and(boolean condition, Consumer<LambdaQueryWrapper<T>> consumer) {
        super.and(condition, consumer);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> and(Consumer<LambdaQueryWrapper<T>> consumer) {
        super.and(consumer);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderBy(boolean condition, boolean isAsc, SFunction<T, ?> column) {
        super.orderBy(condition, isAsc, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderBy(boolean condition, boolean isAsc, List<SFunction<T, ?>> columns) {
        super.orderBy(condition, isAsc, columns);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByAsc(SFunction<T, ?> column) {
        return orderByAsc(true, column);
    }

    @Override
    public LambdaQueryWrapperX<T> orderByAsc(boolean condition, SFunction<T, ?> column) {
        super.orderByAsc(condition, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByAsc(List<SFunction<T, ?>> columns) {
        return orderByAsc(true, columns);
    }

    @Override
    public LambdaQueryWrapperX<T> orderByAsc(boolean condition, List<SFunction<T, ?>> columns) {
        super.orderByAsc(condition, columns);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByDesc(SFunction<T, ?> column) {
        return orderByDesc(true, column);
    }

    @Override
    public LambdaQueryWrapperX<T> orderByDesc(boolean condition, SFunction<T, ?> column) {
        super.orderByDesc(condition, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> orderByDesc(List<SFunction<T, ?>> columns) {
        return orderByDesc(true, columns);
    }

    @Override
    public LambdaQueryWrapperX<T> orderByDesc(boolean condition, List<SFunction<T, ?>> columns) {
        super.orderByDesc(condition, columns);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> groupBy(SFunction<T, ?> column) {
        return groupBy(true, column);
    }

    @Override
    public LambdaQueryWrapperX<T> groupBy(boolean condition, SFunction<T, ?> column) {
        super.groupBy(condition, column);
        return this;
    }

    @Override
    public LambdaQueryWrapperX<T> groupBy(List<SFunction<T, ?>> columns) {
        return groupBy(true, columns);
    }

    @Override
    public LambdaQueryWrapperX<T> groupBy(boolean condition, List<SFunction<T, ?>> columns) {
        super.groupBy(condition, columns);
        return this;
    }
}
