package system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepyu.module.system.dao.dict.SystemDictData;
import top.sheepyu.server.SheepyuServer;

import java.util.HashSet;

/**
 * @author ygq
 * @date 2023-01-17 10:26
 **/
@SpringBootTest(classes = SheepyuServer.class)
public class DictTest {
    @Test
    public void test() {
        HashSet<SystemDictData> set = new HashSet<>();
        SystemDictData data1 = new SystemDictData();
        data1.setDictType("common_status").setValue("1");

        SystemDictData data2 = new SystemDictData();
        data2.setDictType("common_status").setValue("0");

        set.add(data1);
        set.add(data2);

        System.out.println(set);
        set.remove(new SystemDictData().setDictType("common_status").setValue("0"));
        System.out.println(set);
    }
}
