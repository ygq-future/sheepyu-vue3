package top.sheepyu.module.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }

    public static String getRootCauseMessage(Throwable th) {
        return cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage(th);
    }
}
