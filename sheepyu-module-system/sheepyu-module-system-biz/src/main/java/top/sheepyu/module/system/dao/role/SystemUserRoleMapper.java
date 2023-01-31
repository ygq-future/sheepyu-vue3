package top.sheepyu.module.system.dao.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-29 17:54
 **/
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {
    default void deleteByRoleId(Long id) {
        delete(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getRoleId, id));
    }

    default List<Long> findRoleIdByUserId(Long userId) {
        return selectList(new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getUserId, userId)
                .select(SystemUserRole::getRoleId))
                .stream().map(SystemUserRole::getRoleId)
                .collect(Collectors.toList());
    }
}
