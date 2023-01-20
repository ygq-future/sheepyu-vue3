package top.sheepyu.module.system.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.security.core.LoginUser;
import top.sheepyu.framework.security.core.service.SecurityRedisService;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserQueryVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.accesslog.SystemAccessLogService;
import top.sheepyu.module.system.service.captcha.CaptchaService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.CODE_ERROR;
import static top.sheepyu.module.system.enums.LoginLogTypeEnum.LOGIN_USERNAME;
import static top.sheepyu.module.system.enums.LoginResultEnum.CAPTCHA_CODE_ERROR;

/**
 * @author ygq
 * @date 2023-01-18 15:15
 **/
@Service
@Validated
public class SystemUserBiz {
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SecurityRedisService securityRedisService;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private SystemAccessLogService systemAccessLogService;

    public LoginUser login(SystemUserLoginVo loginVo) {
        //校验验证码
        if (!captchaService.verifyCaptcha(loginVo.getKey(), loginVo.getCode())) {
            systemAccessLogService.createAccessLog(null, loginVo.getUsername(), null, LOGIN_USERNAME, CAPTCHA_CODE_ERROR);
            throw exception(CODE_ERROR);
        }

        //校验用户名密码
        SystemUser user = systemUserService.login(loginVo);
        LoginUser loginUser = new LoginUser().setId(user.getId());
        loginUser.setUsername(user.getUsername()).setUserType(user.getType());

        //将用户存入redis并设置访问和刷新令牌
        securityRedisService.setLoginUser(loginUser);
        //更新用户登录时间
        systemUserService.updateLoginTime(user);
        return loginUser;
    }

    public void logout() {
        LoginUser loginUser = SecurityFrameworkUtil.getLoginUser();
        if (loginUser != null) {
            securityRedisService.delLoginUser(loginUser.getAccessToken(), loginUser.getRefreshToken());
        }
    }

    public SystemUser info() {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        return systemUserService.insensitiveInfo(userId);
    }

    public PageResult<SystemUser> pageUser(@Valid SystemUserQueryVo queryVo) {
        //TODO 根据postId和deptId查询用户id, 并用in查询
        return systemUserService.page(queryVo, buildQuery(queryVo));
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
        return systemUserService.list(buildQuery(queryVo));
    }

    private LambdaQueryWrapper<SystemUser> buildQuery(SystemUserQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        boolean keywordExists = StrUtil.isNotBlank(keyword);
        return systemUserService.buildQuery()
                .eqIfPresent(SystemUser::getStatus, queryVo.getStatus())
                .betweenIfPresent(SystemUser::getLoginTime, queryVo.getLoginTimes())
                .betweenIfPresent(SystemUser::getCreateTime, queryVo.getCreateTimes())
                .and(keywordExists, e -> e.like(SystemUser::getUsername, keyword).or()
                        .like(SystemUser::getNickname, keyword).or()
                        .like(SystemUser::getMobile, keyword).or()
                        .like(SystemUser::getEmail, keyword));
    }
}
