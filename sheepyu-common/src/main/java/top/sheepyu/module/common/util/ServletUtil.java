package top.sheepyu.module.common.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import top.sheepyu.module.common.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ygq
 * @date 2022-08-27 16:16
 **/
public class ServletUtil {
    public static void response(HttpServletResponse res, Result<Object> result) throws IOException {
        res.setContentType("application/json;charset=utf8");
        Date time = result.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject jsonObject = new JSONObject(JSONUtil.toJsonStr(result));
        jsonObject.set("time", format.format(time));
        res.getWriter().println(jsonObject);
    }

    public static String getClientIp(HttpServletRequest request) {
        String clientIP = cn.hutool.extra.servlet.ServletUtil.getClientIP(request);
        return "0:0:0:0:0:0:0:1".equals(clientIP) ? "127.0.0.1" : clientIP;
    }
}
