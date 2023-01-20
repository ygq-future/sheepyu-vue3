package top.sheepyu.module.system.service.accesslog;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.accesslog.SystemAccessLog;
import top.sheepyu.module.system.enums.LoginLogTypeEnum;
import top.sheepyu.module.system.enums.LoginResultEnum;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
public interface SystemAccessLogService extends IServiceX<SystemAccessLog> {
    void createAccessLog(Long userId, String username, String nickname, LoginLogTypeEnum logType, LoginResultEnum result);
}
