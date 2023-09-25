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

    void changeStatus(Long id);

    void deleteMenu(String ids);

    List<SystemMenu> listMenu(SystemMenuQueryVo queryVo);

    List<SystemMenu> convertToTree(List<SystemMenu> list);

    SystemMenu findById(Long id);

    Set<Long> listMenuIdFromCache();

    /**
     * 从缓存中根据 menuIds 获取对应的权限字符串
     *
     * @param menuIds menuIds
     * @param enable  是否只查询状态为开启的菜单
     * @return Set<String>
     */
    Set<String> findPermissionByMenuIdsFromCache(Set<Long> menuIds, boolean enable);

    /**
     * 从缓存中根据 menuIds 获取对应的菜单对象
     *
     * @param menuIds menuIds
     * @param enable  是否只查询状态为开启的菜单
     * @return List<SystemMenu>
     */
    List<SystemMenu> findMenuByIdsFromCache(Set<Long> menuIds, boolean enable);
}
