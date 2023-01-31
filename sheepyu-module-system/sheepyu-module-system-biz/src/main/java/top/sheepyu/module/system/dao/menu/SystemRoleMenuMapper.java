package top.sheepyu.module.system.dao.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ygq
 * @date 2023-01-29 17:53
 **/
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {
    default void deleteByMenuId(Long id) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>().eq(SystemRoleMenu::getMenuId, id));
    }

    default void deleteByRoleId(Long id) {
        delete(new LambdaQueryWrapper<SystemRoleMenu>().eq(SystemRoleMenu::getRoleId, id));

    }
}
