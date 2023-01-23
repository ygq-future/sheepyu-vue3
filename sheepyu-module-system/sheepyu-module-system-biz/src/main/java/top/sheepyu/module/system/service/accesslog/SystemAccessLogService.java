package top.sheepyu.module.system.service.accesslog;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.accesslog.vo.SystemAccessLogQueryVo;
import top.sheepyu.module.system.dao.accesslog.SystemAccessLog;
import top.sheepyu.module.system.enums.LoginLogTypeEnum;
import top.sheepyu.module.system.enums.LoginResultEnum;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
public interface SystemAccessLogService extends IServiceX<SystemAccessLog> {
    void createAccessLog(Long userId, String username, String nickname, LoginLogTypeEnum logType, LoginResultEnum result);

    PageResult<SystemAccessLog> pageAccessLog(@Valid SystemAccessLogQueryVo queryVo);
}
