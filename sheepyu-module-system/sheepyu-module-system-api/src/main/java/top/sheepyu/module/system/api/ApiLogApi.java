package top.sheepyu.module.system.api;

import top.sheepyu.module.system.dto.ApiLogDto;

/**
 * @author ygq
 * @date 2023-01-18 11:14
 **/
public interface ApiLogApi {
    void createApiLog(ApiLogDto apiLog);
}
