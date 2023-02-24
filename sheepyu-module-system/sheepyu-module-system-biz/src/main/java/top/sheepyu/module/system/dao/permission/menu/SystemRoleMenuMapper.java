package top.sheepyu.module.system.dao.permission.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-29 17:53
 **/
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {
    default void deleteByMenuIds(Collection<Long> ids) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>().in(SystemRoleMenu::getMenuId, ids));
    }

    default void deleteByRoleIds(Collection<Long> ids) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>().in(SystemRoleMenu::getRoleId, ids));
    }

    default Set<Long> findMenuIdByRoleId(Long roleId) {
        return selectList(new LambdaQueryWrapper<SystemRoleMenu>()
                .eq(SystemRoleMenu::getRoleId, roleId)
                .select(SystemRoleMenu::getMenuId))
                .stream().map(SystemRoleMenu::getMenuId)
                .collect(Collectors.toSet());
    }

    default void deleteMenuByRoleId(Long roleId, Collection<Long> remove) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>()
                .eq(SystemRoleMenu::getRoleId, roleId)
                .in(SystemRoleMenu::getMenuId, remove));
    }

    default void insertMenuByRoleId(Long roleId, Collection<Long> add) {
        for (Long menuId : add) {
            insert(new SystemRoleMenu().setRoleId(roleId).setMenuId(menuId));
        }
    }
}
