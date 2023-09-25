package top.sheepyu.module.system.dao.permission.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.util.CollectionUtil.convertSet;

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
        List<SystemRoleMenu> roleMenus = selectList(new LambdaQueryWrapper<SystemRoleMenu>()
                .eq(SystemRoleMenu::getRoleId, roleId)
                .select(SystemRoleMenu::getMenuId));
        return convertSet(roleMenus, SystemRoleMenu::getMenuId);
    }

    default void deleteMenuByRoleId(Long roleId, Collection<Long> menuIdList) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>()
                .eq(SystemRoleMenu::getRoleId, roleId)
                .in(SystemRoleMenu::getMenuId, menuIdList));
    }

    default void insertMenuByRoleId(Long roleId, Collection<Long> menuIdList) {
        for (Long menuId : menuIdList) {
            insert(new SystemRoleMenu().setRoleId(roleId).setMenuId(menuId));
        }
    }
}
