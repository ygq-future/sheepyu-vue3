package top.sheepyu.framework.security.core.service;

import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.system.api.PermissionApi;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-17 16:43
 **/
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {
    @Resource
    private PermissionApi permissionApi;

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        return permissionApi.hasAnyPermissions(SecurityFrameworkUtil.getLoginUserId(), permissions);
    }

    @Override
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        return permissionApi.hasAnyRoles(SecurityFrameworkUtil.getLoginUserId(), roles);
    }
}
