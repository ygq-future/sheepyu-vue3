package top.sheepyu.module.system.service.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.menu.SystemMenuMapper;
import top.sheepyu.module.system.dao.permission.menu.SystemRoleMenuMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.MENU_HAS_CHILDREN;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.MENU_NOT_EXISTS;
import static top.sheepyu.module.system.convert.permission.SystemMenuConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
public class SystemMenuServiceImpl extends ServiceImplX<SystemMenuMapper, SystemMenu> implements SystemMenuService {
    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;
    private static final Map<Long, SystemMenu> MENUS = new HashMap<>();
    private static final Map<String, SystemMenu> PERMISSIONS = new HashMap<>();

    @Override
    public void createMenu(SystemMenuCreateVo createVo) {
        SystemMenu menu = CONVERT.convert(createVo);
        save(menu);
        MENUS.put(menu.getId(), menu);
        PERMISSIONS.put(menu.getPermission(), menu);
    }

    @Override
    public void updateMenu(SystemMenuUpdateVo updateVo) {
        SystemMenu menu = CONVERT.convert(updateVo);
        boolean result = updateById(menu);
        if (result) {
            menu = findByIdValidateExists(menu.getId());
            MENUS.put(menu.getId(), menu);
            PERMISSIONS.put(menu.getPermission(), menu);
        }
    }

    @Transactional
    @Override
    public void deleteMenu(Long id) {
        SystemMenu menu = findByIdValidateExists(id);
        if (menu == null) {
            return;
        }

        if (existsChildren(id)) {
            throw exception(MENU_HAS_CHILDREN);
        }
        removeById(id);
        //同时删除关联数据
        systemRoleMenuMapper.deleteByMenuId(id);
        MENUS.remove(menu.getId());
        PERMISSIONS.remove(menu.getPermission());
    }

    @Override
    public List<SystemMenu> listMenu(SystemMenuQueryVo queryVo) {
        List<SystemMenu> list = list(buildQuery()
                .likeIfPresent(SystemMenu::getName, queryVo.getKeyword())
                .eqIfPresent(SystemMenu::getStatus, queryVo.getStatus())
                .orderByAsc(SystemMenu::getSort));

        //筛选出一级目录
        List<SystemMenu> result = new ArrayList<>();
        for (SystemMenu menu : list) {
            if (Objects.equals(menu.getParentId(), 0L)) {
                result.add(menu);
            }
        }

        //递归封装数据
        for (SystemMenu menu : result) {
            menu.setChildren(fillTreeData(list, menu.getId()));
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public SystemMenu findById(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public Set<Long> listMenuIdFromCache() {
        return MENUS.keySet();
    }

    @Override
    public List<SystemMenu> findMenuByIdsFromCache(Set<Long> menuIds) {
        return menuIds.stream().map(MENUS::get).collect(Collectors.toList());
    }

    private List<SystemMenu> fillTreeData(List<SystemMenu> list, Long id) {
        List<SystemMenu> result = new ArrayList<>();

        for (SystemMenu menu : list) {
            if (Objects.equals(menu.getParentId(), id)) {
                result.add(menu);
            }
        }

        for (SystemMenu menu : result) {
            menu.setChildren(fillTreeData(list, menu.getId()));
        }

        return result;
    }

    private boolean existsChildren(Long id) {
        return lambdaQuery().eq(SystemMenu::getParentId, id).exists();
    }

    private SystemMenu findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, MENU_NOT_EXISTS);
    }

    @PostConstruct
    public void initMenus() {
        log.info("初始化加载菜单缓存");
        List<SystemMenu> list = list();
        for (SystemMenu menu : list) {
            MENUS.put(menu.getId(), menu);
            PERMISSIONS.put(menu.getPermission(), menu);
        }
    }
}
