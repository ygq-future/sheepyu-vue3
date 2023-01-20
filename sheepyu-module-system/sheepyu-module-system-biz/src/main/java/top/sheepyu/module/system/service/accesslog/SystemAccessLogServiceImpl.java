package top.sheepyu.module.system.service.accesslog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.dao.accesslog.SystemAccessLog;
import top.sheepyu.module.system.dao.accesslog.SystemAccessLogMapper;
import top.sheepyu.module.system.enums.LoginLogTypeEnum;
import top.sheepyu.module.system.enums.LoginResultEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
@Service
@Slf4j
public class SystemAccessLogServiceImpl extends ServiceImplX<SystemAccessLogMapper, SystemAccessLog> implements SystemAccessLogService {
    @Override
    public void createAccessLog(Long userId, String username, String nickname, LoginLogTypeEnum logType, LoginResultEnum result) {
        SystemAccessLog accessLog = new SystemAccessLog();
        accessLog.setUserId(userId).setUsername(username).setNickname(nickname).setLoginResult(result.getCode());
        accessLog.setUserType(WebFrameworkUtil.getLoginUserType()).setLoginType(logType.getCode());

        HttpServletRequest request = WebFrameworkUtil.getRequest();
        String userAgent = request.getHeader("User-Agent");
        String clientIp = ServletUtil.getClientIp(request);
        accessLog.setUserAgent(userAgent).setUserIp(clientIp);
        save(accessLog);
    }
}
