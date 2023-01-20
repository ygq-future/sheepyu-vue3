package top.sheepyu.module.system.api;

import org.springframework.stereotype.Service;
import top.sheepyu.module.system.dto.ApiLogDto;
import top.sheepyu.module.system.service.apilog.SystemApiLogService;

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
