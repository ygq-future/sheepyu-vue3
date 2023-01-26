package top.sheepyu.module.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

/**
 * @author ygq
 * @date 2023-01-25 18:05
 **/
@Slf4j
public class FileUtil {
    public static final Tika TIKA = new Tika();

    public static String getMimeType(byte[] data, String filename) {
        return TIKA.detect(data, filename);
    }

    public static String getMimeType(String filename) {
        return TIKA.detect(filename);
    }

    public static void writeFromStream(InputStream in, File dest) {
        cn.hutool.core.io.FileUtil.writeFromStream(in, dest);
    }

    public static void writeBytes(byte[] data, File dest) {
        cn.hutool.core.io.FileUtil.writeBytes(data, dest);
    }

    public static String getSuffix(String filename) {
        return "." + cn.hutool.core.io.FileUtil.getSuffix(filename);
    }

    public static int getSize(InputStream in) {
        int size = 0;
        try {
            size = in.available();
        } catch (IOException e) {
            log.error("获取文件大小错误: {}", e.getMessage());
        }
        return size;
    }

    public static byte[] readAllData(InputStream is) {
        byte[] data;
        try (InputStream in = is) {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            log.error("文件内容读取失败!");
            throw new RuntimeException(e);
        }
        return data;
    }

    public static void merge(Enumeration<InputStream> es, String mergeFile) {
        try (SequenceInputStream sis = new SequenceInputStream(es);
             BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(mergeFile)))) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = sis.read(bytes)) > 0) {
                out.write(bytes, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream createIn(String path) {
        try {
            return Files.newInputStream(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
