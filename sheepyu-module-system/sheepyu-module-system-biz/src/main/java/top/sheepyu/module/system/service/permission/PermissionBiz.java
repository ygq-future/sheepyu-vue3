package top.sheepyu.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.menu.SystemRoleMenu;
import top.sheepyu.module.system.dao.permission.menu.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemUserRole;
import top.sheepyu.module.system.dao.permission.role.SystemUserRoleMapper;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.user.SystemUserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;
import static top.sheepyu.module.common.util.CollectionUtil.convertSet;
import static top.sheepyu.module.common.util.CollectionUtil.convertSetFilter;

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
    private static final Map<Long, Set<Long>> userRolesCache = new HashMap<>();
    private static final Map<Long, Set<Long>> roleMenusCache = new HashMap<>();
    private static final Map<Long, Set<Long>> menuRolesCache = new HashMap<>();

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
    public void assignMenu(Long roleId, Set<Long> menuIds) {
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
        //加载缓存
        loadRoleMenuRole();
    }

    /**
     * 修改用户的角色, 即可以添加用户角色, 也可以删除用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id, 这个就代表用户拥有的角色,
     *                例如[1, 2]那么修改之后用户只有1, 2两个角色的权限, 因为在修改过程中有删除, 也有添加
     */
    @Transactional
    public void assignRole(Long userId, Set<Long> roleIds) {
        Set<Long> oldRoleIds = systemUserRoleMapper.findRoleIdByUserId(userId);

        //过滤出需要添加的角色
        Collection<Long> add = CollUtil.subtract(roleIds, oldRoleIds);
        //过滤出需要删除的角色
        Collection<Long> remove = CollUtil.subtract(oldRoleIds, roleIds);

        if (CollUtil.isNotEmpty(remove)) {
            systemUserRoleMapper.deleteRoleByUserId(userId, remove);
        }

        if (CollUtil.isNotEmpty(add)) {
            systemUserRoleMapper.insertRoleByUserId(userId, add);
        }
        //加载缓存
        loadUserRole(userId);
    }

    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return Arrays.stream(permissions).anyMatch(e -> hasPermission(userId, e));
    }

    public boolean hasPermission(Long userId, String permission) {
        Set<Long> roleIds = userRolesCache.get(userId);
        if (systemRoleService.isAnySuperAdmin(roleIds)) {
            return true;
        }
        Set<Long> menuIds = getMenuIdsByRoleIds(roleIds);
        List<SystemMenu> menus = systemMenuService.findMenuByIdsFromCache(menuIds);
        return convertSetFilter(menus, SystemMenu::getPermission,
                e -> Objects.equals(e.getStatus(), ENABLE.getCode()))
                .contains(permission);
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
        List<SystemRole> roles = systemRoleService.findRoleByIdsFromCache(roleIds);
        return convertSetFilter(roles, SystemRole::getCode,
                e -> Objects.equals(ENABLE.getCode(), e.getStatus()))
                .stream().anyMatch(e -> ArrayUtil.contains(roleCodes, e));
    }

    public List<SystemRole> findRoleByUserId(Long userId) {
        Set<Long> roleIds = userRolesCache.get(userId);
        return systemRoleService.findRoleByIdsFromCache(roleIds);
    }

    @PostConstruct
    public void initCache() {
        log.info("加载权限缓存...");

        loadUserRole();
        loadRoleMenuRole();

        log.info("加载权限缓存完成");
    }

    private void loadUserRole() {
        log.info("加载用户角色缓存...");
        Set<Long> userIds = convertSet(systemUserService.list(systemUserService
                .buildQuery()
                .select(SystemUser::getId)
                .eq(SystemUser::getType, ADMIN.getCode())), SystemUser::getId);
        List<SystemUserRole> userRoles = systemUserRoleMapper.selectList(null);

        //加载用户对应的角色
        for (Long userId : userIds) {
            Set<Long> roleIds = convertSetFilter(userRoles, SystemUserRole::getRoleId,
                    e -> Objects.equals(userId, e.getUserId()));
            userRolesCache.put(userId, roleIds);
        }
        log.info("加载用户角色缓存完成");
    }

    private void loadRoleMenuRole() {
        log.info("加载角色菜单缓存...");
        Set<Long> roleIds = convertSet(systemRoleService.lambdaQuery()
                .select(SystemRole::getId).list(), SystemRole::getId);
        Set<Long> menuIds = convertSet(systemMenuService.lambdaQuery()
                .select(SystemMenu::getId).list(), SystemMenu::getId);
        List<SystemRoleMenu> roleMenus = systemRoleMenuMapper.selectList(null);

        //加载角色对应的菜单
        for (Long roleId : roleIds) {
            Set<Long> roleMenuIds = convertSetFilter(roleMenus, SystemRoleMenu::getMenuId,
                    e -> Objects.equals(roleId, e.getRoleId()));
            roleMenusCache.put(roleId, roleMenuIds);
        }
        //加载菜单对应的角色
        for (Long menuId : menuIds) {
            Set<Long> menuRoleIds = convertSetFilter(roleMenus, SystemRoleMenu::getRoleId,
                    e -> Objects.equals(menuId, e.getMenuId()));
            roleMenusCache.put(menuId, menuRoleIds);
        }
        log.info("加载角色菜单缓存完成");
    }

    private void loadUserRole(Long userId) {
        log.info("授权角色, 加载用户角色缓存");
        Set<Long> roleIds = systemUserRoleMapper.findRoleIdByUserId(userId);
        userRolesCache.put(userId, roleIds);
    }
}
