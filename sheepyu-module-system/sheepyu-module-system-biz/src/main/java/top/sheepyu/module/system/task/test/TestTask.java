package top.sheepyu.module.system.task.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.job.core.handler.JobHandler;

/**
 * @author ygq
 * @date 2023-01-17 10:15
 **/
@Component
@Slf4j
public class TestTask implements JobHandler {
    @Override
    public String execute(String param) {
        log.info("test任务执行..., param: {}", param);
        return null;
    }
}
