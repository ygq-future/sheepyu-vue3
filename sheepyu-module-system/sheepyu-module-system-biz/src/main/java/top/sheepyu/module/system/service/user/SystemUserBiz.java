package top.sheepyu.module.system.service.user;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.service.SecurityRedisService;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.user.vo.*;
import top.sheepyu.module.system.controller.app.user.vo.AppUserLoginVo;
import top.sheepyu.module.system.controller.app.user.vo.AppUserRegisterVo;
import top.sheepyu.module.system.controller.app.user.vo.EmailLoginVo;
import top.sheepyu.module.system.convert.user.SystemUserConvert;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;
import top.sheepyu.module.system.dao.user.SystemUserRoleMapper;
import top.sheepyu.module.system.service.captcha.CaptchaService;
import top.sheepyu.module.system.service.dept.SystemDeptService;
import top.sheepyu.module.system.service.log.SystemAccessLogService;
import top.sheepyu.module.system.service.permission.PermissionBiz;

import javax.validation.Valid;
import java.util.*;

import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.REFRESH_TOKEN_KEY;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.OPERATION_FAILED;
import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;
import static top.sheepyu.module.common.enums.UserTypeEnum.MEMBER;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.CODE_ERROR;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.EMAIL_OR_MOBILE_NONNULL;
import static top.sheepyu.module.system.enums.log.LoginResultEnum.CAPTCHA_CODE_ERROR;
import static top.sheepyu.module.system.enums.log.LoginResultEnum.SUCCESS;
import static top.sheepyu.module.system.enums.log.LoginTypeEnum.LOGIN_TOKEN;
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
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final SystemUserDeptMapper systemUserDeptMapper;
    private final PermissionBiz permissionBiz;

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
        //记录日志
        systemAccessLogService.createAccessLog(loginUser.getId(), loginUser.getUsername(), null, LOGIN_TOKEN, SUCCESS);
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

    @Transactional(rollbackFor = Exception.class)
    public void createUser(SystemUserCreateVo createVo) {
        createVo.setType(ADMIN.getCode());
        Long userId = systemUserService.create(createVo);
        permissionBiz.assignDeptToUser(userId, createVo.getDeptIds());
    }

    /**
     * 用于用户端注册
     *
     * @param registerVo 用户创建vo
     */
    public void registerUser(@Valid AppUserRegisterVo registerVo) {
        //邮箱或者手机号一者一定不能为空
        if (StrUtil.isAllBlank(registerVo.getEmail(), registerVo.getMobile())) {
            throw exception(EMAIL_OR_MOBILE_NONNULL);
        }

        SystemUserCreateVo createVo = new SystemUserCreateVo();
        createVo.setNickname(registerVo.getNickname());
        createVo.setPassword(registerVo.getPassword());
        createVo.setEmail(registerVo.getEmail());
        createVo.setMobile(registerVo.getMobile());
        createVo.setType(MEMBER.getCode());
        createVo.setStatus(ENABLE.getCode());
        //设置用户名
        createVo.setUsername("sheepyu_user" + RandomUtil.randomNumbers(12));
        systemUserService.create(createVo);
    }

    public SystemUser infoById(Long id) {
        SystemUser userInfo = systemUserService.insensitiveInfo(id);
        fillDeptInfo(userInfo);
        return userInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SystemUserUpdateVo updateVo) {
        systemUserService.updateUser(updateVo);
        Long userId = updateVo.getId();
        permissionBiz.assignDeptToUser(userId, updateVo.getDeptIds());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        systemUserService.deleteUser(id);
        systemUserRoleMapper.deleteByUserId(id);
        systemUserDeptMapper.deleteByUserId(id);
    }

    public List<SystemUser> listUser(@Valid SystemUserQueryVo queryVo) {
        if (queryVo.getType() == null) {
            queryVo.setType(2);
        }
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

        Set<Long> userIds = new HashSet<>();
        //非超级管理员只能查询自己部门下的用户
        if (!permissionBiz.isSuperAdminRole()) {
            Long userId = SecurityFrameworkUtil.getLoginUserId();
            userIds.addAll(systemDeptService.deepQueryUserIdByUserId(userId));
            userIds.add(0L);
        }
        //添加指定部门下的所有用户id
        if (queryVo.getDeptId() != null) {
            Set<Long> singleDeptId = Collections.singleton(queryVo.getDeptId());
            userIds.addAll(systemDeptService.deepQueryUserIdByDeptId(singleDeptId));
            userIds.add(0L);
        }

        return systemUserService.buildQuery()
                .eqIfPresent(SystemUser::getStatus, queryVo.getStatus())
                .betweenIfPresent(SystemUser::getCreateTime, queryVo.getCreateTimes())
                .betweenIfPresent(SystemUser::getLoginTime, queryVo.getLoginTimes())
                .eqIfPresent(SystemUser::getType, queryVo.getType())
                .inIfPresent(SystemUser::getId, userIds)
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemUser::getUsername, keyword).or()
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

    public void updatePassword(SystemUpdatePassVo updatePassVo) {
        Long userId = SecurityFrameworkUtil.getLoginUserId();
        systemUserService.updatePassword(userId, updatePassVo.getOldPass(), updatePassVo.getNewPass());
    }

    private void fillDeptInfo(SystemUser user) {
        Long userId = user.getId();
        Set<Long> deptIds = systemUserDeptMapper.findDeptIdByUserId(userId);
        user.setDeptIds(deptIds);
        List<String> deptNames = systemDeptService.findNamesByIds(userId, deptIds);
        user.setDeptNames(String.join(",", deptNames));
    }

    public SystemUserStatisticsVo statistics() {
        SystemUserStatisticsVo vo = new SystemUserStatisticsVo();
        Date now = new Date();
        DateTime lastDay = DateUtil.offsetDay(now, -1);
        long total = systemUserService.count();
        //获取今日注册人数
        long todayIncrement = systemUserService.count(systemUserService
                .buildQuery()
                .between(SystemUser::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        //获取昨天增加的人数
        long lastDayIncrement = systemUserService.count(systemUserService
                .buildQuery()
                .between(SystemUser::getCreateTime, DateUtil.beginOfDay(lastDay), DateUtil.endOfDay(lastDay))
        );
        long difference = (todayIncrement - lastDayIncrement);
        if (difference < 0L) difference = 0L;
        int todayPercent = (int) (difference / (lastDayIncrement == 0 ? 1 : lastDayIncrement)) * 100;
        vo.setTotal(total);
        vo.setTodayIncrement(todayIncrement);
        vo.setTodayPercent(todayPercent);
        //获取上周开始时间和结束时间
        Date beginWeek = DateUtil.beginOfWeek(DateUtil.lastWeek());
        Date endWeek = DateUtil.endOfWeek(DateUtil.lastWeek());
        //获取上周注册量统计
        List<Long> weekIncrement = systemUserService.countByWeek(beginWeek, endWeek);
        //获取上周访问量
        List<Long> weekAccess = systemAccessLogService.countByWeek(beginWeek, endWeek);
        vo.setWeekIncrement(weekIncrement);
        vo.setWeekAccess(weekAccess);
        //获取最近注册的5个用户
        List<SystemUser> userList = systemUserService.page(new PageParam(1, 5), systemUserService
                .buildQuery()
                .select(SystemUser::getNickname, SystemUser::getAvatar, SystemUser::getCreateTime)
                .orderByDesc(SystemUser::getCreateTime)).getList();
        List<SystemUserRespVo> nearUserList = SystemUserConvert.CONVERT.convertList(userList);
        vo.setNearUserList(nearUserList);
        return vo;
    }
}
