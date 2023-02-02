package top.sheepyu.module.system.api.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.module.system.service.permission.PermissionBiz;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-15 15:48
 **/
@Service
@Slf4j
public class PermissionApiImpl implements PermissionApi {
    @Resource
    private PermissionBiz permissionBiz;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionBiz.hasAnyPermissions(userId, permissions);
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return permissionBiz.hasAnyRoles(userId, roles);
    }
}
