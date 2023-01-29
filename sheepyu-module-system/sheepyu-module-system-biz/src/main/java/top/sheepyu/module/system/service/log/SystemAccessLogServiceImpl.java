package top.sheepyu.module.system.service.log;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogQueryVo;
import top.sheepyu.module.system.dao.log.SystemAccessLog;
import top.sheepyu.module.system.dao.log.SystemAccessLogMapper;
import top.sheepyu.module.system.enums.log.LoginTypeEnum;
import top.sheepyu.module.system.enums.log.LoginResultEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
@Service
@Slf4j
@Validated
public class SystemAccessLogServiceImpl extends ServiceImplX<SystemAccessLogMapper, SystemAccessLog> implements SystemAccessLogService {
    @Override
    public void createAccessLog(Long userId, String username, String nickname, LoginTypeEnum logType, LoginResultEnum result) {
        SystemAccessLog accessLog = new SystemAccessLog();
        accessLog.setUserId(userId).setUsername(username).setNickname(nickname).setLoginResult(result.getCode());
        accessLog.setUserType(WebFrameworkUtil.getLoginUserType()).setLoginType(logType.getCode());

        HttpServletRequest request = WebFrameworkUtil.getRequest();
        String userAgent = request.getHeader("User-Agent");
        String clientIp = ServletUtil.getClientIp(request);
        accessLog.setUserAgent(userAgent).setUserIp(clientIp);
        save(accessLog);
    }

    @Override
    public PageResult<SystemAccessLog> pageAccessLog(SystemAccessLogQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        boolean keywordExists = StrUtil.isNotBlank(keyword);
        return page(queryVo, buildQuery()
                .eqIfPresent(SystemAccessLog::getUserType, queryVo.getUserType())
                .eqIfPresent(SystemAccessLog::getLoginType, queryVo.getLoginType())
                .eqIfPresent(SystemAccessLog::getLoginResult, queryVo.getLoginResult())
                .and(keywordExists, q -> q.like(SystemAccessLog::getUsername, keyword).or()
                        .like(SystemAccessLog::getNickname, keyword)));
    }
}
