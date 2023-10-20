package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.user.SystemUserRoleMapper;
import top.sheepyu.module.system.service.permission.bo.SystemRoleQueryBo;

import javax.annotation.Resource;
import java.util.*;

import static top.sheepyu.module.common.common.PageResult.emptyPage;
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
    public void deleteRole(Collection<Long> idList) {
        if (hasAnySuperAdmin(new HashSet<>(idList))) {
            throw exception(DONT_REMOVE_SUPER_ROLE);
        }
        batchDelete(idList, SystemRole::getId);

        //同步删除用户角色数据和角色菜单数据
        systemUserRoleMapper.deleteByRoleIds(idList);
        systemDeptRoleMapper.deleteByRoleIds(idList);
        systemRoleMenuMapper.deleteByRoleIds(idList);
    }

    @Override
    public void transfer(Long sourceDeptId, Long targetDeptId) {
        lambdaUpdate()
                .set(SystemRole::getDeptId, targetDeptId)
                .eq(SystemRole::getDeptId, sourceDeptId)
                .update();
    }

    @Override
    public PageResult<SystemRole> pageAllRole(SystemRoleQueryBo queryBo) {
        Set<Long> deptIds = queryBo.getDeptIds();
        if (queryBo.getDeptId() != null && CollUtil.isEmpty(deptIds)) {
            return emptyPage();
        }
        String keyword = queryBo.getKeyword();
        //只有超级管理员才能查看所有角色, 其他的人只能查看自己部门下人员创建的角色
        return page(queryBo, buildQuery()
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemRole::getName, keyword).or()
                        .like(SystemRole::getCode, keyword))
                .in(queryBo.getDeptId() != null, SystemRole::getDeptId, deptIds)
                .orderByAsc(SystemRole::getSort));
    }

    @Override
    public PageResult<SystemRole> pageRoleByPermission(SystemRoleQueryBo queryBo) {
        String keyword = queryBo.getKeyword();
        //只有超级管理员才能查看所有角色, 其他的人只能查看自己部门下人员创建的角色
        return page(queryBo, buildQuery()
                .inIfPresent(SystemRole::getDeptId, queryBo.getDeptIds())
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemRole::getName, keyword).or()
                        .like(SystemRole::getCode, keyword))
                .orderByAsc(SystemRole::getSort));
    }

    @Override
    public List<SystemRole> listAllRole() {
        return list(buildQuery().orderByAsc(SystemRole::getSort));
    }

    @Override
    public List<SystemRole> listRoleByCreators(List<String> creatorList) {
        return lambdaQuery()
                .in(SystemRole::getCreator, creatorList)
                .orderByAsc(SystemRole::getSort)
                .list();
    }

    @Override
    public List<SystemRole> listRoleByDeptIds(Set<Long> deptIds) {
        return lambdaQuery()
                .in(SystemRole::getDeptId, deptIds)
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
