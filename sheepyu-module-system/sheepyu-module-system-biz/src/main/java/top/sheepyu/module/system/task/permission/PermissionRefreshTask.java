package top.sheepyu.module.system.task.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.job.core.handler.JobHandler;
import top.sheepyu.module.system.service.permission.PermissionBiz;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-09-24 21:33
 **/
@Component
@Slf4j
public class PermissionRefreshTask implements JobHandler {
    @Resource
    private PermissionBiz permissionBiz;

    @Override
    public String execute(String param) {
        permissionBiz.initCache();
        log.info("PermissionRefreshTask任务执行..., param: {}", param);
        return null;
    }
}
