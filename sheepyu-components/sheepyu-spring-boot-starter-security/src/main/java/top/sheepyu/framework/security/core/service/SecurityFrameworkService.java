package top.sheepyu.framework.security.core.service;

/**
 * @author ygq
 * @date 2023-01-15 15:50
 **/
public interface SecurityFrameworkService {


    boolean hasPermission(String permission);

    boolean hasAnyPermissions(String... permissions);

    boolean hasRole(String role);

    boolean hasAnyRoles(String... roles);
}
