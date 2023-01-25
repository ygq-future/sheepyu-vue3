package top.sheepyu.module.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
}
