package top.sheepyu.module.system.service.log;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.log.api.SystemApiLogQueryVo;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.api.log.ApiLogDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemApiLogService extends IServiceX<SystemApiLog> {
    void createApiLog(@Valid ApiLogDto apiLogDto);

    PageResult<SystemApiLog> pageApiLog(@Valid SystemApiLogQueryVo queryVo);

    void process(@NotNull(message = "日志id不能为空") Long id);
}
