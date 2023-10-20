package top.sheepyu.module.system.service.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptRole;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenu;
import top.sheepyu.module.system.dao.permission.role.SystemRoleMenuMapper;
import top.sheepyu.module.system.dao.user.*;
import top.sheepyu.module.system.service.dept.SystemDeptService;
import top.sheepyu.module.system.service.permission.bo.SystemRoleQueryBo;
import top.sheepyu.module.system.service.user.SystemUserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static top.sheepyu.framework.security.util.SecurityFrameworkUtil.getLoginUserId;
import static top.sheepyu.module.common.common.PageResult.emptyPage;
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
        return isSuperAdminRole(getLoginUserId());
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

    @Transactional
    public void handleRoleInfo(Long userId) {
        Set<Long> singleUserId = Collections.singleton(userId);
        List<String> usernames = systemUserService.findFieldValueByIds(SystemUser::getUsername, singleUserId);
        //拿到这个用户所有创建的角色信息
        List<SystemRole> roleList = systemRoleService.listRoleByCreators(usernames);
        if (CollUtil.isEmpty(roleList)) return;
        Set<Long> roleIds = convertSet(roleList, SystemRole::getId);
        //获取部门/用户关联的角色id
        Set<Long> relevancyRoleIds = getRelevancyRoleIds();
        //排除掉关联的角色id, 把不关联的删除
        Collection<Long> removeRoleIds = CollUtil.subtract(roleIds, relevancyRoleIds);
        if (CollUtil.isNotEmpty(removeRoleIds)) {
            systemRoleService.deleteRole(removeRoleIds);
        }
        //其他的直接转移的超管
        Collection<Long> transferRoleIds = CollUtil.subtract(roleIds, removeRoleIds);
        if (CollUtil.isNotEmpty(transferRoleIds)) {
            systemRoleService.lambdaUpdate()
                    .set(SystemRole::getCreator, "admin")
                    .in(SystemRole::getId, transferRoleIds)
                    .update();
        }
    }

    @Transactional
    public void deleteRole(String ids) {
        List<Long> roleIds = MyStrUtil.splitToLong(ids, ',');
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        Set<Long> relevancyRoleIds = getRelevancyRoleIds();
        Collection<Long> intersection = CollUtil.intersection(roleIds, relevancyRoleIds);
        if (CollUtil.isNotEmpty(intersection)) {
            throw exception(ROLE_HAS_RELEVANCY);
        }
        systemRoleService.deleteRole(roleIds);
    }

    /**
     * 获取所有被关联的角色的id
     *
     * @return Set<Long>
     */
    private Set<Long> getRelevancyRoleIds() {
        Set<Long> userRoleIds = systemUserRoleMapper.selectRoleIds();
        Set<Long> deptRoleIds = systemDeptRoleMapper.selectRoleIds();
        return CollUtil.unionDistinct(userRoleIds, deptRoleIds);
    }

    /**
     * 角色分页
     * 根据用户的权限查询角色, 如果是管理员角色, 返回所有
     * 否则只返回自己部门下及其子部门下的角色
     *
     * @return PageResult<SystemRole>
     */
    public PageResult<SystemRole> pageRoleByPermission(SystemRoleQueryVo queryVo) {
        SystemRoleQueryBo queryBo = BeanUtil.copyProperties(queryVo, SystemRoleQueryBo.class);
        Long userId = getLoginUserId();
        //获取用户对应的所有角色
        Set<Long> roleIds = findRoleIdsByUserId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return emptyPage();
        }

        //如果带了部门查询条件, 就查询此部门及其子部门下的所有部门id
        Set<Long> deptIds;
        if (queryBo.getDeptId() != null) {
            deptIds = systemDeptService.deepQueryDeptIdByDeptId(Collections.singleton(queryBo.getDeptId()));
            queryBo.setDeptIds(deptIds);
        }

        //如果有超级管理员角色
        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            return systemRoleService.pageAllRole(queryBo);
        }

        //将用户拥有的角色加入条件
        queryBo.setRoleIds(roleIds);
        //如果没有带查询条件, 就查询此用户所在的部门及其所在部门的子部门id
        if (queryBo.getDeptId() == null) {
            deptIds = systemDeptService.deepQueryDeptIdByDeptId(userDeptsCache.get(userId));
            queryBo.setDeptIds(deptIds);
        }
        return systemRoleService.pageRoleByPermission(queryBo);
    }

    /**
     * 根据用户的权限查询角色, 如果是管理员角色, 返回所有
     * 否则只返回自己部门及其子部门下的角色
     *
     * @return List<SystemRole>
     */
    public List<SystemRole> listRoleByPermission() {
        Long userId = getLoginUserId();
        //获取用户对应的所有角色
        Set<Long> roleIds = findRoleIdsByUserId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        //如果有超级管理员角色
        if (systemRoleService.hasAnySuperAdmin(roleIds)) {
            return systemRoleService.listAllRole();
        }

        //获取用户所属部门及其子部门id
        Set<Long> deptIds = systemDeptService.deepQueryDeptIdByDeptId(userDeptsCache.get(userId));
        //如果为空就只返回用户自己所拥有的
        if (CollUtil.isEmpty(deptIds)) {
            return systemRoleService.listByIds(roleIds);
        }
        //根据部门ids查询角色
        List<SystemRole> roleList = systemRoleService.listRoleByDeptIds(deptIds);
        //取出roleId
        Set<Long> creatorRoleIdList = convertSet(roleList, SystemRole::getId);
        //排除重复
        Collection<Long> userRoleIds = CollUtil.subtract(roleIds, creatorRoleIdList);
        if (CollUtil.isNotEmpty(userRoleIds)) {
            roleList.addAll(systemRoleService.listByIds(userRoleIds));
        }
        return roleList;
    }

    private static Set<Long> findRoleIdsByUserIds(Set<Long> userIds) {
        Set<Long> roleIds = new HashSet<>();
        userIds.forEach(userId -> {
            roleIds.addAll(userRolesCache.get(userId));
            Set<Long> deptIds = userDeptsCache.get(userId);
            deptIds.forEach(deptId -> roleIds.addAll(deptRolesCache.get(deptId)));
        });
        return roleIds;
    }

    private Set<Long> findRoleIdsByUserId(Long userId) {
        return findRoleIdsByUserIds(Collections.singleton(userId));
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
        Long userId = getLoginUserId();
        //获取用户对应的所有角色
        Set<Long> roleIds = findRoleIdsByUserId(userId);
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

    /**
     * 获取用户拥有的权限
     *
     * @return 权限列表
     */
    public Set<String> listPermissionByUser() {
        Long userId = getLoginUserId();
        Set<Long> roleIds = findRoleIdsByUserId(userId);
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
        if (menuIds == null) {
            menuIds = new HashSet<>();
        }
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
        } finally {
            roleMenusCacheLock.unlock();
        }
    }

    public void assignUserToDept(Long deptId, Set<Long> userIds) {
        if (userIds == null) {
            userIds = new HashSet<>();
        }
        userDeptsCacheLock.lock();
        try {
            Set<Long> oldUserIds = systemUserDeptMapper.findUserIdByDeptId(deptId);

            //过滤出需要添加的用户
            Collection<Long> add = CollUtil.subtract(userIds, oldUserIds);
            //过滤出需要删除的用户
            Collection<Long> remove = CollUtil.subtract(oldUserIds, userIds);

            if (CollUtil.isNotEmpty(remove)) {
                systemUserDeptMapper.deleteByDeptIdAndUserIds(deptId, remove);
            }

            if (CollUtil.isNotEmpty(add)) {
                systemUserDeptMapper.insertUserIdsByDeptsId(deptId, add);
            }

            log.info("assignUserToDept: 分配部门负责人, 更新用户(部门/职位)缓存");
            add.forEach(userId -> {
                if (userDeptsCache.containsKey(userId)) {
                    userDeptsCache.get(userId).add(deptId);
                } else {
                    HashSet<Long> deptIds = new HashSet<>();
                    deptIds.add(deptId);
                    userDeptsCache.put(userId, deptIds);
                }
            });
            remove.forEach(userId -> {
                if (userDeptsCache.containsKey(userId)) {
                    userDeptsCache.get(userId).remove(deptId);
                }
            });
        } finally {
            userDeptsCacheLock.unlock();
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
        if (deptIds == null) {
            deptIds = new HashSet<>();
        }
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
        if (roleIds == null) {
            roleIds = new HashSet<>();
        }
        userRolesCacheLock.lock();
        try {
            if (userId.equals(getLoginUserId())) {
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
        if (roleIds == null) {
            roleIds = new HashSet<>();
        }
        deptRolesCacheLock.lock();
        try {
            if (systemDeptService.isOwn(getLoginUserId(), deptId)) {
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
        } finally {
            deptRolesCacheLock.unlock();
        }
    }

    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return Arrays.stream(permissions).anyMatch(e -> hasPermission(userId, e));
    }

    public boolean hasPermission(Long userId, String permission) {
        Set<Long> roleIds = findRoleIdsByUserId(userId);
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

    @PostConstruct
    public void initCache() {
        log.info("加载权限缓存...");

        userDeptsCache.clear();
        userRolesCache.clear();
        deptRolesCache.clear();
        roleMenusCache.clear();
        loadDeptRole();
        loadUserRole();
        loadRoleMenuRole();
        systemMenuService.initMenus();

        log.info("加载权限缓存完成");
    }
}
