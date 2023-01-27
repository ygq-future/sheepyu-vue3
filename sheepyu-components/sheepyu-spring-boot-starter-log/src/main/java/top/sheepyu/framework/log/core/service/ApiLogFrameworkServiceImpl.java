package top.sheepyu.framework.log.core.service;

import top.sheepyu.module.system.api.log.ApiLogApi;
import top.sheepyu.module.system.api.log.ApiLogDto;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-17 16:42
 **/
public class ApiLogFrameworkServiceImpl implements ApiLogFrameworkService {
    @Resource
    private ApiLogApi apiLogApi;

    @Override
    public void createApiLog(ApiLogDto apiLog) {
        apiLogApi.createApiLog(apiLog);
    }
}
