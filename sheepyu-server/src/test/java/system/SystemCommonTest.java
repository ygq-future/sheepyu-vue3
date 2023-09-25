package system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepyu.module.system.dao.user.SystemUserDept;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;
import top.sheepyu.module.system.service.dept.SystemDeptService;
import top.sheepyu.server.SheepyuServer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-17 10:26
 **/
@SpringBootTest(classes = SheepyuServer.class)
public class SystemCommonTest {
    @Resource
    private SystemDeptService systemDeptService;
    @Resource
    private SystemUserDeptMapper systemUserDeptMapper;

    @Test
    public void test() {
        List<SystemUserDept> deptList = systemUserDeptMapper.list();
        System.out.println(deptList);
    }
}
