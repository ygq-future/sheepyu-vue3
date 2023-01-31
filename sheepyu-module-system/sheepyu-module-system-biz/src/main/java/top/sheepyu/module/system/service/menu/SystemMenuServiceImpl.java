package top.sheepyu.module.system.service.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.menu.SystemMenu;
import top.sheepyu.module.system.dao.menu.SystemMenuMapper;
import top.sheepyu.module.system.dao.menu.SystemRoleMenuMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.MENU_HAS_CHILDREN;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.MENU_NOT_EXISTS;
import static top.sheepyu.module.system.convert.menu.SystemMenuConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemMenuServiceImpl extends ServiceImplX<SystemMenuMapper, SystemMenu> implements SystemMenuService {
    private final SystemRoleMenuMapper systemRoleMenuMapper;

    @Override
    public void createMenu(SystemMenuCreateVo createVo) {
        SystemMenu menu = CONVERT.convert(createVo);
        save(menu);
    }

    @Override
    public void updateMenu(SystemMenuUpdateVo updateVo) {
        SystemMenu menu = CONVERT.convert(updateVo);
        updateById(menu);
    }

    @Transactional
    @Override
    public void deleteMenu(Long id) {
        if (existsChildren(id)) {
            throw exception(MENU_HAS_CHILDREN);
        }
        removeById(id);
        //同时删除关联数据
        systemRoleMenuMapper.deleteByMenuId(id);
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

}
