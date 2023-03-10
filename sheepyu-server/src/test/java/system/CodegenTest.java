package system;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepyu.module.system.service.codegen.SystemCodegenBiz;
import top.sheepyu.server.SheepyuServer;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author ygq
 * @date 2023-03-05 14:28
 **/
@SpringBootTest(classes = SheepyuServer.class)
public class CodegenTest {
    @Resource
    private SystemCodegenBiz systemCodegenBiz;

    @Test
    public void test() throws IOException {
        Map<String, String> result = systemCodegenBiz.generateCode(114L);
        String[] paths = result.keySet().toArray(new String[]{});
        ByteArrayInputStream[] ins = result.values().stream().map(IoUtil::toUtf8Stream).toArray(ByteArrayInputStream[]::new);
        ZipUtil.zip(Files.newOutputStream(Paths.get("c:\\codegen.zip")), paths, ins);
    }
}
