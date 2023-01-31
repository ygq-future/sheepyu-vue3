package top.sheepyu.module.system.service.role;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.menu.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.role.SystemRole;
import top.sheepyu.module.system.dao.role.SystemRoleMapper;
import top.sheepyu.module.system.dao.role.SystemUserRoleMapper;

import java.util.List;

import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.ROLE_NOT_EXISTS;
import static top.sheepyu.module.system.convert.role.SystemRoleConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemRoleServiceImpl extends ServiceImplX<SystemRoleMapper, SystemRole> implements SystemRoleService {
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;

    @Override
    public void createRole(SystemRoleCreateVo createVo) {
        SystemRole role = CONVERT.convert(createVo);
        save(role);
    }

    @Override
    public void updateRole(SystemRoleUpdateVo updateVo) {
        SystemRole role = CONVERT.convert(updateVo);
        updateById(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        removeById(id);
        //同步删除用户角色数据和角色菜单数据
        systemUserRoleMapper.deleteByRoleId(id);
        systemRoleMenuMapper.deleteByRoleId(id);
    }

    @Override
    public PageResult<SystemRole> pageRole(SystemRoleQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .likeIfPresent(SystemRole::getName, queryVo.getKeyword())
                .eqIfPresent(SystemRole::getStatus, queryVo.getStatus())
                .orderByAsc(SystemRole::getSort));
    }

    @Override
    public List<SystemRole> listRole() {
        return list(buildQuery().orderByAsc(SystemRole::getSort));
    }

    @Override
    public SystemRole findById(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public List<SystemRole> listRoleByUserId(Long userId) {
        List<Long> roleIds = systemUserRoleMapper.findRoleIdByUserId(userId);
        return list(buildQuery().in(SystemRole::getId, roleIds)
                .eq(SystemRole::getStatus, ENABLE.getCode()));
    }

    private SystemRole findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, ROLE_NOT_EXISTS);
    }

}
