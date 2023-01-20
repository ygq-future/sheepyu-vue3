package top.sheepyu.module.system.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserMapper;

import javax.annotation.Resource;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.user.SystemUserConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-18 14:40
 **/
@Service
@Slf4j
@Validated
public class SystemUserServiceImpl extends ServiceImplX<SystemUserMapper, SystemUser> implements SystemUserService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public SystemUser login(SystemUserLoginVo loginVo) {
        SystemUser user = findByUsername(loginVo.getUsername());
        if (user == null) {
            throw exception(LOGIN_FAILED);
        }

        //密码不匹配
        if (!passwordEncoder.matches(loginVo.getPassword(), user.getPassword())) {
            throw exception(LOGIN_FAILED);
        }
        return user;
    }

    @Override
    public SystemUser findByUsername(String username) {
        return lambdaQuery().eq(SystemUser::getUsername, username).one();
    }

    @Override
    public SystemUser insensitiveInfo(Long userId) {
        SystemUser user = findByIdValidateExists(userId);
        user.setPassword(null);
        return user;
    }

    @Override
    public void create(SystemUserCreateVo createVo) {
        SystemUser user = CONVERT.convert(createVo);
        SystemUser byUsername = findByUsername(user.getUsername());
        if (byUsername != null) {
            throw exception(USER_EXISTS);
        }
        //根据用户名删除deleted=1的用户
        baseMapper.removeByUsernameDeleted(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
    }

    @Override
    public void updateUser(SystemUserUpdateVo updateVo) {
        findByIdValidateExists(updateVo.getId());
        SystemUser user = CONVERT.convert(updateVo);
        user.setUsername(null);
        updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        removeById(id);
    }

    private SystemUser findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, USER_NOT_EXISTS);
    }

}
