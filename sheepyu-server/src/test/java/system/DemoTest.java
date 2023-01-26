package system;

import cn.hutool.crypto.digest.MD5;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author ygq
 * @date 2023-01-24 16:09
 **/
public class DemoTest {
    public static void main(String[] args) throws IOException {
        //52c41a705e5f476fe9d25fc8d80a76cb
        try (InputStream in = Files.newInputStream(new File("F:\\avatar\\1\\1.jpg").toPath())) {
            byte[] data = new byte[in.available()];
            in.read(data);

            String md5 = MD5.create().digestHex(data);
            System.out.println(md5);
        }
    }
}
