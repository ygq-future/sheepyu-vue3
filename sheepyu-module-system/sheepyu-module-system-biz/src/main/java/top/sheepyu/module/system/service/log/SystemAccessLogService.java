package top.sheepyu.module.system.service.log;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogQueryVo;
import top.sheepyu.module.system.dao.log.SystemAccessLog;
import top.sheepyu.module.system.enums.log.LoginTypeEnum;
import top.sheepyu.module.system.enums.log.LoginResultEnum;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
public interface SystemAccessLogService extends IServiceX<SystemAccessLog> {
    void createAccessLog(Long userId, String username, String nickname, LoginTypeEnum logType, LoginResultEnum result);

    PageResult<SystemAccessLog> pageAccessLog(@Valid SystemAccessLogQueryVo queryVo);

    List<Integer> countByWeek(Date beginWeek, Date endWeek);
}
