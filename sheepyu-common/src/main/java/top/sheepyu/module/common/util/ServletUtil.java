package top.sheepyu.module.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import top.sheepyu.module.common.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * 给响应文件流的请求设置响应类型,编码和文件名
     *
     * @param res      响应对象
     * @param filename 文件名, 需要带扩展名
     */
    public static void responseFile(HttpServletResponse res, String filename) {
        String suffix = FileUtil.getSuffix(filename);
        filename = filename.substring(0, filename.lastIndexOf('.'));
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy年MM月dd日HH时mm分ss秒"));
        filename += datetime.concat(suffix);
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncodeUtil.encode(filename, StandardCharsets.UTF_8));
        res.setContentType("application/octet-stream;charset=UTF-8");
    }

    /**
     * 给响应文件流的请求设置响应类型,编码和文件名,并传输该文件
     *
     * @param res      响应对象
     * @param filename 文件名, 需要带扩展名
     * @param file     文件
     */
    public static void responseFile(HttpServletResponse res, String filename, File file) {
        String suffix = FileUtil.getSuffix(filename);
        filename = filename.substring(0, filename.lastIndexOf('.'));
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy年MM月dd日HH时mm分ss秒"));
        filename += datetime.concat(suffix);
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncodeUtil.encode(filename, StandardCharsets.UTF_8));
        res.setContentType("application/octet-stream;charset=UTF-8");
        cn.hutool.extra.servlet.ServletUtil.write(res, file);
    }
}
