package top.sheepyu.module.common.util;

import cn.hutool.core.util.StrUtil;

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
}
