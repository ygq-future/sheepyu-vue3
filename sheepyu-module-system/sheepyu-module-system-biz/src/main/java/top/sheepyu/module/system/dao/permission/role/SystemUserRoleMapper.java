package top.sheepyu.module.system.dao.permission.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-29 17:54
 **/
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {
    default void deleteByRoleId(Long id) {
        delete(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getRoleId, id));
    }

    default Set<Long> findRoleIdByUserId(Long userId) {
        return selectList(new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getUserId, userId)
                .select(SystemUserRole::getRoleId))
                .stream().map(SystemUserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    default void deleteRoleByUserId(Long userId, Collection<Long> remove) {
        delete(new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getUserId, userId)
                .in(SystemUserRole::getRoleId, remove));
    }

    default void insertRoleByUserId(Long userId, Collection<Long> add) {
        for (Long roleId : add) {
            insert(new SystemUserRole().setUserId(userId).setRoleId(roleId));
        }
    }
}
