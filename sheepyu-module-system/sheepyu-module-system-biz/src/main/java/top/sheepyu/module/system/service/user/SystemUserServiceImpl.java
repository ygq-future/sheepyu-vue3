package top.sheepyu.module.system.service.user;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.sms.config.SmsSenderFactory;
import top.sheepyu.framework.sms.core.sender.SmsSender;
import top.sheepyu.framework.sms.core.sender.email.EmailParams;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.enums.CommonStatusEnum;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.controller.app.user.vo.EmailLoginVo;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserMapper;
import top.sheepyu.module.system.enums.log.LoginTypeEnum;
import top.sheepyu.module.system.service.config.SystemConfigService;
import top.sheepyu.module.system.service.log.SystemAccessLogService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.user.SystemUserConvert.CONVERT;
import static top.sheepyu.module.system.enums.config.SystemConfigKeyEnum.DEFAULT_PASSWORD;
import static top.sheepyu.module.system.enums.log.LoginResultEnum.*;
import static top.sheepyu.module.system.enums.log.LoginTypeEnum.LOGIN_EMAIL;
import static top.sheepyu.module.system.enums.log.LoginTypeEnum.LOGIN_USERNAME;

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
    @Resource
    private SystemAccessLogService systemAccessLogService;
    @Resource
    private SmsSenderFactory smsSenderFactory;
    @Resource
    private SystemConfigService systemConfigService;

    @Override
    public SystemUser login(String login, String password) {
        //?????????????????????or??????, ????????????username or email or mobile???????????????
        //???????????????, ?????????????????????????????????, ????????????or??????, ?????????????????????
        SystemUser user = findByUsername(login);

        //?????????????????????????????????
        if (user == null) {
            user = findByEmail(login);
        }
        //???????????????, ???????????????????????????
        if (user == null) {
            user = findByMobile(login);
        }
        if (user == null) {
            systemAccessLogService.createAccessLog(null, login, null, LOGIN_USERNAME, BAD_CREDENTIALS);
            throw exception(LOGIN_FAILED);
        }
        checkStatus(user, LOGIN_USERNAME);

        //???????????????
        if (!passwordEncoder.matches(password, user.getPassword())) {
            systemAccessLogService.createAccessLog(null, login, null, LOGIN_USERNAME, BAD_CREDENTIALS);
            throw exception(LOGIN_FAILED);
        }

        systemAccessLogService.createAccessLog(user.getId(), login, user.getNickname(), LOGIN_USERNAME, SUCCESS);
        return user;
    }

    @Override
    public SystemUser findByUsername(String username) {
        return lambdaQuery()
                .eq(SystemUser::getUsername, username)
                .eq(SystemUser::getType, WebFrameworkUtil.getLoginUserType())
                .one();
    }

    @Override
    public SystemUser findByEmail(String email) {
        return lambdaQuery()
                .eq(SystemUser::getEmail, email)
                .eq(SystemUser::getType, WebFrameworkUtil.getLoginUserType())
                .one();
    }

    @Override
    public SystemUser findByMobile(String mobile) {
        return lambdaQuery()
                .eq(SystemUser::getMobile, mobile)
                .eq(SystemUser::getType, WebFrameworkUtil.getLoginUserType())
                .one();
    }

    @Override
    public SystemUser insensitiveInfo(Long userId) {
        SystemUser user = findByIdValidateExists(userId);
        user.setPassword(null);
        return user;
    }

    @Override
    public void create(SystemUserCreateVo createVo) {
        SystemUser user = CONVERT.convert(createVo).setType(ADMIN.getCode());
        SystemUser byUsername = findByUsername(user.getUsername());
        if (byUsername != null) {
            throw exception(USER_EXISTS);
        }
        //?????????????????????deleted=1?????????
        baseMapper.removeByUsernameDeleted(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
    }

    @Override
    public void updateUser(SystemUserUpdateVo updateVo) {
        findByIdValidateExists(updateVo.getId());
        SystemUser user = CONVERT.convert(updateVo);
        updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        removeById(id);
    }

    @Override
    public void updateLoginTime(SystemUser user) {
        String loginIp = ServletUtil.getClientIp(WebFrameworkUtil.getRequest());
        user.setLoginTime(new Date()).setLoginIp(loginIp);
        updateById(user);
    }

    @Override
    public SystemUser loginByEmail(EmailLoginVo loginVo) {
        SmsSender smsSender = smsSenderFactory.get();
        SystemUser user = findByEmail(loginVo.getEmail());
        if (user == null) {
            systemAccessLogService.createAccessLog(null, loginVo.getEmail(), null, LOGIN_USERNAME, BAD_CREDENTIALS);
            throw exception(LOGIN_FAILED);
        }
        //????????????????????????
        checkStatus(user, LOGIN_EMAIL);

        if (!smsSender.verifyCode(loginVo.getEmail(), loginVo.getCode())) {
            systemAccessLogService.createAccessLog(null, user.getEmail(), null, LOGIN_EMAIL, CAPTCHA_CODE_ERROR);
            throw exception(CODE_ERROR);
        }

        systemAccessLogService.createAccessLog(user.getId(), user.getEmail(), user.getNickname(), LOGIN_EMAIL, SUCCESS);
        return user;
    }

    @Override
    public void sendCode(String email) {
        smsSenderFactory.get().sendCode(new EmailParams(email));
    }

    @Override
    public void resetPassword(Long id, String newPass) {
        SystemUser user = findByIdValidateExists(id);
        String password;
        if (StrUtil.isNotBlank(newPass)) {
            password = passwordEncoder.encode(newPass);
        } else {
            String defaultPassword = systemConfigService.get(DEFAULT_PASSWORD);
            password = passwordEncoder.encode(defaultPassword);
        }
        user.setPassword(password);
        updateById(user);
    }

    @Override
    public void updateNickname(Long userId, String nickname) {
        SystemUser user = findByIdValidateExists(userId);
        user.setNickname(nickname);
        updateById(user);
    }

    @Override
    public void updateMobile(Long userId, String mobile) {
        SystemUser user = findByIdValidateExists(userId);
        user.setMobile(mobile);
        updateById(user);
    }

    @Override
    public void updateEmail(Long userId, String email) {
        SystemUser user = findByIdValidateExists(userId);
        user.setEmail(email);
        updateById(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {
        SystemUser user = findByIdValidateExists(userId);
        user.setAvatar(avatar);
        updateById(user);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        SystemUser user = findByIdValidateExists(userId);
        user.setPassword(passwordEncoder.encode(password));
        updateById(user);
    }

    private void checkStatus(SystemUser user, LoginTypeEnum loginType) {
        if (Objects.equals(CommonStatusEnum.DISABLE.getCode(), user.getStatus())) {
            systemAccessLogService.createAccessLog(null, user.getUsername(), null, loginType, USER_DISABLED);
            throw exception(ErrorCodeConstants.USER_DISABLE);
        }
    }

    private SystemUser findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, USER_NOT_EXISTS);
    }

}
