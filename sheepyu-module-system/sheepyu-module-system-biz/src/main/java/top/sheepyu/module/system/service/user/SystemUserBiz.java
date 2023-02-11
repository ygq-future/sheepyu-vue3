package top.sheepyu.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.service.SecurityRedisService;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserQueryVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.controller.app.user.vo.AppUserLoginVo;
import top.sheepyu.module.system.controller.app.user.vo.EmailLoginVo;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.captcha.CaptchaService;
import top.sheepyu.module.system.service.dept.SystemDeptService;
import top.sheepyu.module.system.service.log.SystemAccessLogService;
import top.sheepyu.module.system.service.post.SystemPostService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.REFRESH_TOKEN_KEY;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.OPERATION_FAILED;
import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.CODE_ERROR;
import static top.sheepyu.module.system.enums.log.LoginResultEnum.CAPTCHA_CODE_ERROR;
import static top.sheepyu.module.system.enums.log.LoginTypeEnum.LOGIN_USERNAME;

/**
 * @author ygq
 * @date 2023-01-18 15:15
 **/
@Service
@Validated
@AllArgsConstructor
public class SystemUserBiz {
    private final SystemUserService systemUserService;
    private final SecurityRedisService securityRedisService;
    private final CaptchaService captchaService;
    private final SystemAccessLogService systemAccessLogService;
    private final SystemDeptService systemDeptService;
    private final SystemPostService systemPostService;

    public LoginUser login(@Valid SystemUserLoginVo loginVo) {
        //校验验证码
        if (!captchaService.verifyCaptcha(loginVo.getKey(), loginVo.getCode())) {
            systemAccessLogService.createAccessLog(null, loginVo.getLogin(), null, LOGIN_USERNAME, CAPTCHA_CODE_ERROR);
            throw exception(CODE_ERROR);
        }

        //校验用户名密码
        SystemUser user = systemUserService.login(loginVo.getLogin(), loginVo.getPassword());
        return loginAfterDo(user);
    }

    public LoginUser loginOfApp(@Valid AppUserLoginVo loginVo) {
        //校验用户名密码
        SystemUser user = systemUserService.login(loginVo.getLogin(), loginVo.getPassword());
        return loginAfterDo(user);
    }

    public void logout() {
        LoginUser loginUser = SecurityFrameworkUtil.getLoginUser();
        if (loginUser != null) {
            securityRedisService.delLoginUser(loginUser.getAccessToken(), loginUser.getRefreshToken());
        }
    }

    public LoginUser refreshToken(String refreshToken) {
        LoginUser loginUser = securityRedisService.getLoginUser(REFRESH_TOKEN_KEY, refreshToken);
        if (loginUser == null) {
            throw exception(OPERATION_FAILED);
        }
        //刷新accessToken和refreshToken
        securityRedisService.setLoginUser(loginUser);
        //删除旧的refreshToken
        securityRedisService.delRefreshToken(refreshToken);
        return loginUser;
    }

    public SystemUser info() {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        SystemUser user = systemUserService.insensitiveInfo(userId);
        if (Objects.equals(user.getType(), ADMIN.getCode())) {
            fillDeptInfo(user);
        }
        return user;
    }

    public PageResult<SystemUser> pageUser(@Valid SystemUserQueryVo queryVo) {
        PageResult<SystemUser> page = systemUserService.page(queryVo, buildQuery(queryVo));
        for (SystemUser user : page.getList()) {
            fillDeptInfo(user);
        }
        return page;
    }

    public void createUser(SystemUserCreateVo createVo) {
        systemUserService.create(createVo);
    }

    public SystemUser infoById(Long id) {
        return systemUserService.insensitiveInfo(id);
    }

    public void updateUser(SystemUserUpdateVo updateVo) {
        systemUserService.updateUser(updateVo);
    }

    public void deleteUser(Long id) {
        systemUserService.deleteUser(id);
    }

    public List<SystemUser> listUser(@Valid SystemUserQueryVo queryVo) {
        List<SystemUser> list = systemUserService.list(buildQuery(queryVo));
        for (SystemUser user : list) {
            fillDeptInfo(user);
        }
        return list;
    }

    public LoginUser loginByEmail(@Valid EmailLoginVo loginVo) {
        SystemUser user = systemUserService.loginByEmail(loginVo);
        return loginAfterDo(user);
    }

    public void sendCode(String email) {
        systemUserService.sendCode(email);
    }

    private LoginUser loginAfterDo(SystemUser user) {
        LoginUser loginUser = new LoginUser().setId(user.getId());
        loginUser.setUsername(user.getUsername()).setUserType(user.getType());

        //将用户存入redis并设置访问和刷新令牌
        securityRedisService.setLoginUser(loginUser);
        //更新用户登录时间
        systemUserService.updateLoginTime(user);
        return loginUser;
    }

    private LambdaQueryWrapper<SystemUser> buildQuery(SystemUserQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        boolean keywordExists = StrUtil.isNotBlank(keyword);

        return systemUserService.buildQuery()
                .eqIfPresent(SystemUser::getStatus, queryVo.getStatus())
                .eqIfPresent(SystemUser::getDeptId, queryVo.getDeptId())
                .betweenIfPresent(SystemUser::getCreateTime, queryVo.getCreateTimes())
                .eq(SystemUser::getType, ADMIN.getCode())
                .and(keywordExists, e -> e.like(SystemUser::getUsername, keyword).or()
                        .like(SystemUser::getNickname, keyword).or()
                        .like(SystemUser::getMobile, keyword).or()
                        .like(SystemUser::getEmail, keyword));
    }

    public void resetPassword(Long id, String newPass) {
        systemUserService.resetPassword(id, newPass);
    }

    public void updateNickname(String nickname) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updateNickname(userId, nickname);
    }

    public void updateMobile(String mobile) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updateMobile(userId, mobile);
    }

    public void updateEmail(String email) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updateEmail(userId, email);
    }

    public void updateAvatar(String avatar) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updateAvatar(userId, avatar);
    }

    public void updatePassword(String password) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updatePassword(userId, password);
    }

    private void fillDeptInfo(SystemUser user) {
        if (user.getDeptId() != null) {
            String deptName = systemDeptService.findNameById(user.getDeptId());
            user.setDeptName(deptName);
        }

        if (CollUtil.isNotEmpty(user.getPostIds())) {
            List<String> postNames = systemPostService.findNamesByIds(user.getPostIds());
            user.setPostNames(String.join(",", postNames));
        }
    }
}
