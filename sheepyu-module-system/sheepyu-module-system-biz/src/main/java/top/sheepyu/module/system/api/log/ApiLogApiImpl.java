package top.sheepyu.module.system.api.log;

import org.springframework.stereotype.Service;
import top.sheepyu.module.system.api.log.ApiLogApi;
import top.sheepyu.module.system.api.log.ApiLogDto;
import top.sheepyu.module.system.service.log.SystemApiLogService;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-18 11:16
 **/
@Service
public class ApiLogApiImpl implements ApiLogApi {
    @Resource
    private SystemApiLogService systemApiLogService;

    @Override
    public void createApiLog(ApiLogDto apiLog) {
        systemApiLogService.createApiLog(apiLog);
    }
}
