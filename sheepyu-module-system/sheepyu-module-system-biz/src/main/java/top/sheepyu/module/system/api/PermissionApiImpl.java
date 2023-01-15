package top.sheepyu.module.system.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.module.system.api.PermissionApi;

/**
 * @author ygq
 * @date 2023-01-15 15:48
 **/
@Service
@Slf4j
public class PermissionApiImpl implements PermissionApi {
    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return true;
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return true;
    }
}
