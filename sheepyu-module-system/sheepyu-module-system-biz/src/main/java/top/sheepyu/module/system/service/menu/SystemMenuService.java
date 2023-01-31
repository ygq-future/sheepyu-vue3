package top.sheepyu.module.system.service.menu;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.menu.SystemMenu;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemMenuService extends IServiceX<SystemMenu> {
    void createMenu(@Valid SystemMenuCreateVo createVo);

    void updateMenu(@Valid SystemMenuUpdateVo updateVo);

    void deleteMenu(Long id);

    List<SystemMenu> listMenu(SystemMenuQueryVo queryVo);

    SystemMenu findById(Long id);
}
