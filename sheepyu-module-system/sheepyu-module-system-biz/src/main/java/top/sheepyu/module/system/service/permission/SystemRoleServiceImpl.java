package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemUserRoleMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static top.sheepyu.module.system.constants.ErrorCodeConstants.ROLE_NOT_EXISTS;
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
    private SystemRoleMenuMapper systemRoleMenuMapper;
    private static final Map<Long, SystemRole> ROLES = new ConcurrentHashMap<>();

    @Override
    public void createRole(SystemRoleCreateVo createVo) {
        SystemRole role = CONVERT.convert(createVo);
        save(role);
        ROLES.put(role.getId(), role);
    }

    @Override
    public void updateRole(SystemRoleUpdateVo updateVo) {
        SystemRole role = CONVERT.convert(updateVo);
        boolean result = updateById(role);
        if (result) {
            role = findByIdValidateExists(role.getId());
            ROLES.put(role.getId(), role);
        }
    }

    @Transactional
    @Override
    public void deleteRole(String ids) {
        List<Long> idList = MyStrUtil.splitToLong(ids, ',');
        batchDelete(ids, SystemRole::getId);

        //???????????????????????????????????????????????????
        systemUserRoleMapper.deleteByRoleIds(idList);
        systemRoleMenuMapper.deleteByRoleIds(idList);
        idList.forEach(ROLES::remove);
    }

    @Override
    public PageResult<SystemRole> pageRole(SystemRoleQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        return page(queryVo, buildQuery()
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemRole::getName, keyword).or()
                        .like(SystemRole::getCode, keyword).or()
                        .eq(SystemRole::getId, keyword))
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
    public List<SystemRole> findRoleByIdsFromCache(Set<Long> roleIds) {
        return roleIds.stream().map(ROLES::get).collect(Collectors.toList());
    }

    private SystemRole findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, ROLE_NOT_EXISTS);
    }

    @PostConstruct
    public void initRoles() {
        log.info("???????????????????????????");
        for (SystemRole role : list()) {
            ROLES.put(role.getId(), role);
        }
    }
}
