package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.util.CollectionUtil.convertSet;

/**
 * @author ygq
 * @date 2023-01-29 17:54
 **/
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {
    default void deleteByRoleIds(Collection<Long> ids) {
        delete(new LambdaQueryWrapper<SystemUserRole>().in(SystemUserRole::getRoleId, ids));
    }

    default Set<Long> findRoleIdByUserId(Long userId) {
        List<SystemUserRole> userRoles = selectList(new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getUserId, userId)
                .select(SystemUserRole::getRoleId));
        return convertSet(userRoles, SystemUserRole::getRoleId);
    }

    default void deleteRoleIdByUserId(Long userId, Collection<Long> roleIdList) {
        delete(new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getUserId, userId)
                .in(SystemUserRole::getRoleId, roleIdList));
    }

    default void insertRoleIdsByUserId(Long userId, Collection<Long> roleIdList) {
        for (Long roleId : roleIdList) {
            insert(new SystemUserRole().setUserId(userId).setRoleId(roleId));
        }
    }

    default void deleteByUserId(Long userId) {
        delete(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getUserId, userId));
    }

    default Set<Long> selectRoleIds() {
        return convertSet(selectList(null), SystemUserRole::getRoleId);
    }
}
