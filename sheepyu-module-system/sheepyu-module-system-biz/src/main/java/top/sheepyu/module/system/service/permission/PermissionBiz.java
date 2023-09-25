package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptRole;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenu;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.user.*;
import top.sheepyu.module.system.service.dept.SystemDeptService;
import top.sheepyu.module.system.service.user.SystemUserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.CollectionUtil.convertSet;
import static top.sheepyu.module.common.util.CollectionUtil.convertSetFilter;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;

/**
 * @author ygq
 * @date 2023-01-31 11:03
 **/
@Service
@Slf4j
@Validated
public class PermissionBiz {
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemMenuService systemMenuService;
    @Resource
    private SystemUserRoleMapper systemUserRoleMapper;
    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemDeptService systemDeptService;
    @Resource
    private SystemDeptRoleMapper systemDeptRoleMapper;
    @Resource
    private SystemUserDeptMapper systemUserDeptMapper;
    private static final ConcurrentHashMap<Long, Set<Long>> userDeptsCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Set<Long>> userRolesCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Set<Long>> deptRolesCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Set<Long>> roleMenusCache = new ConcurrentHashMap<>();
    private static final ReentrantLock userDeptsCacheLock = new ReentrantLock();
    private static final ReentrantLock userRolesCacheLock = new ReentrantLock();
    private static final ReentrantLock deptRolesCacheLock = new ReentrantLock();
    private static final ReentrantLock roleMenusCacheLock = new ReentrantLock();

    /**
     * 判断当前登录用户是否有超级管理员角色
     *
     * @return boolean
     */
    public boolean isSuperAdminRole() {
        return isSuperAdminRole(SecurityFrameworkUtil.getLoginUserId());
    }

    /**
     * 判断指定用户是否有超级管理员角色
     *
     * @return boolean
     */
    public boolean isSuperAdminRole(Long userId) {
        Set<Long> roleIds = userRolesCache.get(userId);
        return systemRoleService.hasAnySuperAdmin(roleIds);
    }

    /**
     * 根据用户的权限查询角色, 如果是管理员角色, 返回所有
     * 否则只返回自己创建的和自己拥有的
     *
     * @return List<SystemRole>
     */
    public List<SystemRole> listRoleByPermission() {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        //获取用户对应的所有角色
        Set<Long> roleIds = getUserAllRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            return systemRoleService.listRole();
        }
        //获取用户所创建的角色
        List<SystemRole> roleList = systemRoleService.listRoleByCreator();
        Set<Long> creatorRoleIdList = convertSet(roleList, SystemRole::getId);
        Collection<Long> userRoleIds = CollUtil.subtract(roleIds, creatorRoleIdList);
        if (CollUtil.isNotEmpty(userRoleIds)) {
            roleList.addAll(systemRoleService.listByIds(userRoleIds));
        }
        return roleList;
    }

    public Set<Long> findRoleByUserId(Long userId) {
        return userRolesCache.get(userId);
    }

    public Set<Long> findRoleByDeptId(Long deptId) {
        return deptRolesCache.get(deptId);
    }

    /**
     * 获取用户的菜单
     *
     * @return 菜单列表
     */
    public List<SystemMenu> listMenuByUser() {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        //获取用户对应的所有角色
        Set<Long> roleIds = getUserAllRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            Set<Long> menuIds = systemMenuService.listMenuIdFromCache();
            List<SystemMenu> list = systemMenuService.findMenuByIdsFromCache(menuIds, true);
            return systemMenuService.convertToTree(list);
        }

        //获取角色对应的菜单
        Set<Long> menuIds = getMenuIdsByRoleIds(roleIds);
        List<SystemMenu> list = systemMenuService.findMenuByIdsFromCache(menuIds, true);
        return systemMenuService.convertToTree(list);
    }

    private Set<Long> getUserAllRoleId(Long userId) {
        Set<Long> roleIds = userRolesCache.get(userId);
        Set<Long> deptIds = userDeptsCache.get(userId);
        for (Long deptId : deptIds) {
            roleIds.addAll(deptRolesCache.get(deptId));
        }
        return roleIds;
    }

    /**
     * 获取用户拥有的权限
     *
     * @return 权限列表
     */
    public Set<String> listPermissionByUser() {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        Set<Long> roleIds = getUserAllRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }

        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            return systemMenuService.findPermissionByMenuIdsFromCache(systemMenuService.listMenuIdFromCache(), true);
        }

        //获取角色对应的菜单
        Set<Long> menuIds = getMenuIdsByRoleIds(roleIds);
        return systemMenuService.findPermissionByMenuIdsFromCache(menuIds, true);
    }

    public Set<Long> listMenuIdByRoleId(Long roleId) {
        Set<Long> menuIds;
        if (systemRoleService.isSuperAdmin(roleId)) {
            menuIds = systemMenuService.listMenuIdFromCache();
        } else {
            menuIds = systemRoleMenuMapper.findMenuIdByRoleId(roleId);
        }
        return menuIds;
    }

    @Transactional
    public void assignMenuToRole(Long roleId, Set<Long> menuIds) {
        roleMenusCacheLock.lock();
        try {
            Set<Long> oldMenuIds = systemRoleMenuMapper.findMenuIdByRoleId(roleId);

            //过滤出需要添加的菜单
            Collection<Long> add = CollUtil.subtract(menuIds, oldMenuIds);
            //过滤出需要删除的菜单
            Collection<Long> remove = CollUtil.subtract(oldMenuIds, menuIds);

            if (CollUtil.isNotEmpty(remove)) {
                systemRoleMenuMapper.deleteMenuByRoleId(roleId, remove);
            }

            if (CollUtil.isNotEmpty(add)) {
                systemRoleMenuMapper.insertMenuByRoleId(roleId, add);
            }

            log.info("assignMenuToRole: 分配角色菜单, 更新角色菜单缓存");
            roleMenusCache.put(roleId, menuIds);
        } catch (Exception e) {
            throw exception(ASSIGN_MENU_TO_ROLE_FAILED);
        } finally {
            roleMenusCacheLock.unlock();
        }
    }

    /**
     * 修改用户的(部门/职位), 即可以添加用户的(部门/职位), 也可以删除用户的(部门/职位)
     *
     * @param userId  用户id
     * @param deptIds (部门/职位)id, 这个就代表用户所属的(部门/职位),
     */
    @Transactional
    public void assignDeptToUser(Long userId, Set<Long> deptIds) {
        userDeptsCacheLock.lock();
        try {
            Set<Long> oldDeptIds = systemUserDeptMapper.findDeptIdByUserId(userId);

            //过滤出需要添加的(部门/职位)
            Collection<Long> add = CollUtil.subtract(deptIds, oldDeptIds);
            //过滤出需要删除的(部门/职位)
            Collection<Long> remove = CollUtil.subtract(oldDeptIds, deptIds);

            if (CollUtil.isNotEmpty(remove)) {
                systemUserDeptMapper.deleteByUserIdAndDeptIds(userId, remove);
            }

            if (CollUtil.isNotEmpty(add)) {
                systemUserDeptMapper.insertDeptIdsByUserId(userId, add);
            }

            log.info("assignDeptToUser: 分配用户的(部门/职位), 更新用户(部门/职位)缓存");
            userDeptsCache.put(userId, deptIds);
        } catch (Exception e) {
            throw exception(ASSIGN_DEPT_TO_USER_FAILED);
        } finally {
            userDeptsCacheLock.unlock();
        }
    }

    /**
     * 修改用户的角色, 即可以添加用户角色, 也可以删除用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id, 这个就代表用户拥有的角色,
     *                例如[1, 2]那么修改之后用户只有1, 2两个角色的权限, 因为在修改过程中有删除, 也有添加
     */
    @Transactional
    public void assignRoleToUser(Long userId, Set<Long> roleIds) {
        userRolesCacheLock.lock();
        try {
            if (userId.equals(SecurityFrameworkUtil.getLoginUserId())) {
                throw exception(ASSIGN_TARGET_IS_OWN);
            }
            Set<Long> oldRoleIds = systemUserRoleMapper.findRoleIdByUserId(userId);

            //过滤出需要添加的角色
            Collection<Long> add = CollUtil.subtract(roleIds, oldRoleIds);
            //过滤出需要删除的角色
            Collection<Long> remove = CollUtil.subtract(oldRoleIds, roleIds);

            if (CollUtil.isNotEmpty(remove)) {
                systemUserRoleMapper.deleteRoleIdByUserId(userId, remove);
            }

            if (CollUtil.isNotEmpty(add)) {
                systemUserRoleMapper.insertRoleIdsByUserId(userId, add);
            }

            log.info("assignRoleToUser: 分配用户角色, 更新用户角色缓存");
            userRolesCache.put(userId, roleIds);
        } catch (Exception e) {
            throw exception(ASSIGN_ROLE_TO_USER_FAILED);
        } finally {
            userRolesCacheLock.unlock();
        }
    }

    /**
     * 修改(部门/职位)的角色, 即可以添加角色, 也可以删除角色
     *
     * @param deptId  部门id
     * @param roleIds 角色id, 这个就代表(部门/职位)拥有的角色,
     *                例如[1, 2]那么修改之后(部门/职位)只有1, 2两个角色的权限, 因为在修改过程中有删除, 也有添加
     */
    @Transactional
    public void assignRoleToDept(Long deptId, Set<Long> roleIds) {
        deptRolesCacheLock.lock();
        try {
            if (systemDeptService.isOwn(SecurityFrameworkUtil.getLoginUserId(), deptId)) {
                throw exception(ASSIGN_TARGET_IS_OWN);
            }
            Set<Long> oldRoleIds = systemDeptRoleMapper.findRoleIdByDeptId(deptId);

            //过滤出需要添加的角色
            Collection<Long> add = CollUtil.subtract(roleIds, oldRoleIds);
            //过滤出需要删除的角色
            Collection<Long> remove = CollUtil.subtract(oldRoleIds, roleIds);

            if (CollUtil.isNotEmpty(remove)) {
                systemDeptRoleMapper.deleteRoleIdByDeptId(deptId, remove);
            }

            if (CollUtil.isNotEmpty(add)) {
                systemDeptRoleMapper.insertRoleIdsByDeptId(deptId, add);
            }

            log.info("assignRoleToDept: 分配(部门/职位)角色, 更新(部门/职位)角色缓存");
            deptRolesCache.put(deptId, roleIds);
        } catch (Exception e) {
            throw exception(ASSIGN_ROLE_TO_DEPT_FAILED);
        } finally {
            deptRolesCacheLock.unlock();
        }
    }

    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return Arrays.stream(permissions).anyMatch(e -> hasPermission(userId, e));
    }

    public boolean hasPermission(Long userId, String permission) {
        Set<Long> roleIds = getUserAllRoleId(userId);
        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            return true;
        }
        Set<Long> menuIds = getMenuIdsByRoleIds(roleIds);
        List<SystemMenu> menus = systemMenuService.findMenuByIdsFromCache(menuIds, true);
        return convertSet(menus, SystemMenu::getPermission).contains(permission);
    }

    private Set<Long> getMenuIdsByRoleIds(Set<Long> roleIds) {
        Set<Long> result = new HashSet<>();
        for (Long roleId : roleIds) {
            result.addAll(roleMenusCache.get(roleId));
        }
        return result;
    }

    public boolean hasAnyRoles(Long userId, String... roleCodes) {
        Set<Long> roleIds = userRolesCache.get(userId);
        List<SystemRole> roles = systemRoleService.findRoleByIds(roleIds);
        return convertSet(roles, SystemRole::getCode)
                .stream()
                .anyMatch(e -> ArrayUtil.contains(roleCodes, e));
    }

    @PostConstruct
    public void initCache() {
        log.info("加载权限缓存...");

        loadDeptRole();
        loadUserRole();
        loadRoleMenuRole();

        log.info("加载权限缓存完成");
    }

    private void loadDeptRole() {
        log.info("加载部门角色缓存...");
        Set<Long> deptIds = convertSet(systemDeptService.list(systemDeptService
                .buildQuery().select(SystemDept::getId)), SystemDept::getId);
        List<SystemDeptRole> deptRoles = systemDeptRoleMapper.selectList(null);
        //加载部门对应的角色
        for (Long deptId : deptIds) {
            Set<Long> roleIds = convertSetFilter(deptRoles, SystemDeptRole::getRoleId,
                    e -> Objects.equals(deptId, e.getDeptId()));
            deptRolesCache.put(deptId, roleIds);
        }
        log.info("加载部门角色缓存完成");
    }

    private void loadUserRole() {
        log.info("加载用户角色/用户部门缓存...");
        List<SystemUser> systemUsers = systemUserService.lambdaQuery()
                .select(SystemUser::getId)
                .eq(SystemUser::getType, ADMIN.getCode())
                .list();
        Set<Long> userIds = convertSet(systemUsers, SystemUser::getId);
        List<SystemUserRole> userRoles = systemUserRoleMapper.selectList(null);
        List<SystemUserDept> userDepts = systemUserDeptMapper.selectList(null);

        //加载用户对应的角色和部门
        for (Long userId : userIds) {
            Set<Long> roleIds = convertSetFilter(userRoles, SystemUserRole::getRoleId,
                    e -> Objects.equals(userId, e.getUserId()));
            Set<Long> deptIds = convertSetFilter(userDepts, SystemUserDept::getDeptId,
                    e -> Objects.equals(userId, e.getUserId()));
            userRolesCache.put(userId, roleIds);
            userDeptsCache.put(userId, deptIds);
        }
        log.info("加载用户角色/用户部门缓存完成");
    }

    private void loadRoleMenuRole() {
        log.info("加载角色菜单缓存...");
        List<SystemRole> systemRoles = systemRoleService.lambdaQuery()
                .select(SystemRole::getId)
                .list();
        Set<Long> roleIds = convertSet(systemRoles, SystemRole::getId);
        List<SystemRoleMenu> roleMenus = systemRoleMenuMapper.selectList(null);

        //加载角色对应的菜单
        for (Long roleId : roleIds) {
            Set<Long> roleMenuIds = convertSetFilter(roleMenus, SystemRoleMenu::getMenuId,
                    e -> Objects.equals(roleId, e.getRoleId()));
            roleMenusCache.put(roleId, roleMenuIds);
        }
        log.info("加载角色菜单缓存完成");
    }
}
