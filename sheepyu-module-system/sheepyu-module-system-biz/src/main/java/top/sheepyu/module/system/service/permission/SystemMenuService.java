package top.sheepyu.module.system.service.permission;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemMenuService extends IServiceX<SystemMenu> {
    void createMenu(@Valid SystemMenuCreateVo createVo);

    void updateMenu(@Valid SystemMenuUpdateVo updateVo);

    void deleteMenu(String ids);

    List<SystemMenu> listMenu(SystemMenuQueryVo queryVo);

    List<SystemMenu> convertToTree(List<SystemMenu> list);

    SystemMenu findById(Long id);

    Set<Long> listMenuIdFromCache();

    Set<String> findPermissionByMenuIdsFromCache(Set<Long> menuIds, boolean enable);

    List<SystemMenu> findMenuByIdsFromCache(Set<Long> menuIds, boolean enable);
}
