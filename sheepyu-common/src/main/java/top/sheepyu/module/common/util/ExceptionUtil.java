package top.sheepyu.module.common.util;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ExceptionUtil {
    public static String getMessage(Throwable e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            IoUtil.close(sw);
            IoUtil.close(pw);
        }
        return sw.toString();
    }

    public static String getRootCauseMessage(Throwable th) {
        return cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage(th);
    }
}
