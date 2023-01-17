package system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepyu.server.SheepyuServer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author ygq
 * @date 2023-01-17 10:26
 **/
@SpringBootTest(classes = SheepyuServer.class)
public class SystemJobTest {
    @Test
    public void test() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime secondAfter = now.plusSeconds(1);

        long nowTime = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long afterTime = secondAfter.toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(afterTime - nowTime);
    }
}
