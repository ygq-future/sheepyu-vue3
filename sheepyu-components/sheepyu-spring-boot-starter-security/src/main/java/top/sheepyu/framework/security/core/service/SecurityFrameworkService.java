package top.sheepyu.framework.security.core.service;

import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.system.api.PermissionApi;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-15 15:50
 **/
public class SecurityFrameworkService {
    @Resource
    private PermissionApi permissionApi;

    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    public boolean hasAnyPermissions(String... permissions) {
        return permissionApi.hasAnyPermissions(SecurityFrameworkUtil.getLoginUserId(), permissions);
    }

    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    public boolean hasAnyRoles(String... roles) {
        return permissionApi.hasAnyRoles(SecurityFrameworkUtil.getLoginUserId(), roles);
    }
}
