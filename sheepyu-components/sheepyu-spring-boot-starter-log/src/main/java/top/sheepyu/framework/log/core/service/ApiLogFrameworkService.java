package top.sheepyu.framework.log.core.service;

import top.sheepyu.module.system.api.log.ApiLogDto;

/**
 * 操作日志 Framework Service 接口
 *
 */
public interface ApiLogFrameworkService {

    /**
     * 记录操作日志
     *
     * @param apiLog 操作日志请求
     */
    void createApiLog(ApiLogDto apiLog);

}
