package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.util.CollectionUtil.convertSet;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemUserDeptMapper extends BaseMapper<SystemUserDept> {
    default Set<Long> findDeptIdByUserId(Long userId) {
        LambdaQueryWrapper<SystemUserDept> wrapper = new LambdaQueryWrapper<SystemUserDept>()
                .in(SystemUserDept::getUserId, userId)
                .select(SystemUserDept::getDeptId);
        return convertSet(selectList(wrapper), SystemUserDept::getDeptId);
    }

    default Set<Long> findUserIdByDeptIds(Set<Long> deptIdList) {
        LambdaQueryWrapper<SystemUserDept> wrapper = new LambdaQueryWrapper<SystemUserDept>()
                .in(SystemUserDept::getDeptId, deptIdList)
                .select(SystemUserDept::getUserId);
        return convertSet(selectList(wrapper), SystemUserDept::getUserId);
    }

    default void insertDeptIdsByUserId(Long userId, Collection<Long> deptIds) {
        for (Long deptId : deptIds) {
            insert(new SystemUserDept().setUserId(userId).setDeptId(deptId));
        }
    }

    default void deleteByUserIdAndDeptIds(Long userId, Collection<Long> deptIds) {
        delete(new LambdaQueryWrapper<SystemUserDept>()
                .eq(SystemUserDept::getUserId, userId)
                .in(SystemUserDept::getDeptId, deptIds));
    }

    default void deleteByUserId(Long userId) {
        delete(new LambdaQueryWrapper<SystemUserDept>().eq(SystemUserDept::getUserId, userId));
    }

    List<SystemUserDept> list();

    default boolean existsUser(Long deptId) {
        return exists(new LambdaQueryWrapper<SystemUserDept>().eq(SystemUserDept::getDeptId, deptId));
    }
}
