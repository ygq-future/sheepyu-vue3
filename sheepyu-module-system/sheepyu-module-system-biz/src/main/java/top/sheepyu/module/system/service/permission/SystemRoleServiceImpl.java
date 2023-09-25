package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.user.SystemUserRoleMapper;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static top.sheepyu.framework.security.util.SecurityFrameworkUtil.getLoginUserUsername;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.permission.SystemRoleConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
public class SystemRoleServiceImpl extends ServiceImplX<SystemRoleMapper, SystemRole> implements SystemRoleService {
    @Resource
    private SystemUserRoleMapper systemUserRoleMapper;
    @Resource
    private SystemDeptRoleMapper systemDeptRoleMapper;
    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;

    @Override
    public void createRole(SystemRoleCreateVo createVo) {
        findByFieldThrowIfExists(SystemRole::getCode, createVo.getCode(), ROLE_EXISTS);
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
    public void deleteRole(String ids) {
        List<Long> idList = MyStrUtil.splitToLong(ids, ',');
        if (hasAnySuperAdmin(new HashSet<>(idList))) {
            throw exception(DONT_REMOVE_SUPER_ROLE);
        }
        batchDelete(ids, SystemRole::getId);

        //同步删除用户角色数据和角色菜单数据
        systemUserRoleMapper.deleteByRoleIds(idList);
        systemDeptRoleMapper.deleteByRoleIds(idList);
        systemRoleMenuMapper.deleteByRoleIds(idList);
    }

    @Override
    public PageResult<SystemRole> pageRole(SystemRoleQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        //只有超级管理员才能查看所有角色, 其他的人只能查看自己创建的角色
        boolean superAdmin = SecurityFrameworkUtil.isSuperAdmin();
        return page(queryVo, buildQuery()
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemRole::getName, keyword).or()
                        .like(SystemRole::getCode, keyword).or()
                        .eq(SystemRole::getId, keyword))
                .eq(!superAdmin, SystemRole::getCreator, getLoginUserUsername())
                .orderByAsc(SystemRole::getSort));
    }

    @Override
    public List<SystemRole> listRole() {
        return list(buildQuery().orderByAsc(SystemRole::getSort));
    }

    @Override
    public List<SystemRole> listRoleByCreator() {
        return lambdaQuery()
                .eq(SystemRole::getCreator, getLoginUserUsername())
                .orderByAsc(SystemRole::getSort)
                .list();
    }

    @Override
    public SystemRole findById(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    @Override
    public boolean isSuperAdmin(Long roleId) {
        return roleId == 1L;
    }

    @Override
    public boolean hasAnySuperAdmin(Set<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        return roleIds.stream().anyMatch(this::isSuperAdmin);
    }

    @Override
    public List<SystemRole> findRoleByIds(Set<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(SystemRole::getId, roleIds).list();
    }

    private SystemRole findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, ROLE_NOT_EXISTS);
    }
}
