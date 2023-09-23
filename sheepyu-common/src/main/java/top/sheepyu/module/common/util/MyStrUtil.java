package top.sheepyu.module.common.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-17 11:16
 **/
public class MyStrUtil {
    public static List<String> split(String str, char ch) {
        return StrUtil.split(str, ch).stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }

    public static List<Long> splitToLong(String str, char ch) {
        return StrUtil.split(str, ch).stream().filter(StrUtil::isNotBlank)
                .map(Long::parseLong).collect(Collectors.toList());
    }

    /**
     * 格式: /yyyy/MM/dd/
     *
     * @return 字符串路径
     */
    public static String getDatePath() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/"));
    }

    public static String getUUID() {
        return UUID.fastUUID().toString(true);
    }
}
