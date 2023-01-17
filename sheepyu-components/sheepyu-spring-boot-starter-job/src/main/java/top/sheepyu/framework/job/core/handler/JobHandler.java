package top.sheepyu.framework.job.core.handler;

/**
 * @author ygq
 * @date 2022-12-01 19:10
 **/
public interface JobHandler {
    /**
     * 执行任务
     *
     * @param param 参数
     * @return 结果
     * @throws Exception 异常
     */
    String execute(String param) throws Exception;
}
