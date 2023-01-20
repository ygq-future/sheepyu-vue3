package top.sheepyu.module.system.service.user;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.dao.user.SystemUser;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-18 14:40
 **/
public interface SystemUserService extends IServiceX<SystemUser> {
    SystemUser login(@Valid SystemUserLoginVo loginVo);

    SystemUser findByUsername(@NotBlank(message = "用户名不能为空") String username);

    /**
     * 用户脱敏信息
     *
     * @param userId 用户id
     * @return 用户
     */
    SystemUser insensitiveInfo(@NotNull(message = "用户id不能为空") Long userId);

    void create(@Valid SystemUserCreateVo createVo);

    void updateUser(@Valid SystemUserUpdateVo updateVo);

    void deleteUser(@NotNull(message = "用户id不能为空") Long id);

    void updateLoginTime(SystemUser user);
}
