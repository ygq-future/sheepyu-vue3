package top.sheepyu.module.system.service.log;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.dto.ApiLogDto;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemApiLogService extends IServiceX<SystemApiLog> {
    void createApiLog(@Valid ApiLogDto apiLogDto);
}
