package system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileStatisticsVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserStatisticsVo;
import top.sheepyu.module.system.service.file.SystemFileService;
import top.sheepyu.module.system.service.user.SystemUserBiz;
import top.sheepyu.server.SheepyuServer;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-17 10:26
 **/
@SpringBootTest(classes = SheepyuServer.class)
public class StatisticsTest {
    @Resource
    private SystemFileService systemFileService;
    @Resource
    private SystemUserBiz systemUserBiz;

    @Test
    public void test() {
        SystemFileStatisticsVo statistics = systemFileService.statistics();
        System.out.println(statistics);
        statistics.getWeekIncrement().forEach(System.out::println);

        SystemUserStatisticsVo statistics1 = systemUserBiz.statistics();
        System.out.println(statistics1);
//        Date now = new Date();
//        Date beginWeek = DateUtil.beginOfWeek(now).toJdkDate();
//        Date endWeek = DateUtil.endOfWeek(now).toJdkDate();
//        System.out.println(systemAccessLogService.countByWeek(beginWeek, endWeek));
    }
}
