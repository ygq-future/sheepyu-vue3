package top.sheepyu.module.system.dao.dept;

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
public interface SystemDeptRoleMapper extends BaseMapper<SystemDeptRole> {
    default void deleteByRoleIds(Collection<Long> ids) {
        delete(new LambdaQueryWrapper<SystemDeptRole>().in(SystemDeptRole::getRoleId, ids));
    }

    default Set<Long> findRoleIdByDeptId(Long deptId) {
        List<SystemDeptRole> deptRoles = selectList(new LambdaQueryWrapper<SystemDeptRole>()
                .eq(SystemDeptRole::getRoleId, deptId)
                .select(SystemDeptRole::getRoleId));
        return convertSet(deptRoles, SystemDeptRole::getRoleId);
    }

    default void deleteRoleIdByDeptId(Long deptId, Collection<Long> roleIdList) {
        delete(new LambdaQueryWrapper<SystemDeptRole>()
                .eq(SystemDeptRole::getDeptId, deptId)
                .in(SystemDeptRole::getRoleId, roleIdList));
    }

    default void insertRoleIdsByDeptId(Long deptId, Collection<Long> roleIdList) {
        for (Long roleId : roleIdList) {
            insert(new SystemDeptRole().setDeptId(deptId).setRoleId(roleId));
        }
    }
}
