package top.sheepyu.module.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-19 14:24
 **/
public class ExcelUtil {
    /**
     * 导出excel文件, 文件名
     *
     * @param filename 不用带后缀
     */
    public static <T> void write(HttpServletResponse response, String filename, Class<T> excelDataType, List<T> data) throws IOException {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy年MM月dd日HH时mm分ss秒"));
        filename += datetime.concat(".xlsx");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        EasyExcel.write(response.getOutputStream(), excelDataType)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .sheet().doWrite(data);
    }

    /**
     * 导入excel
     * @param file 文件
     * @param excelDataType 接受数据的ExcelVo
     * @return 返回数据列表
     */
    public static <T> List<T> read(MultipartFile file, Class<T> excelDataType) throws IOException {
        return EasyExcel.read(file.getInputStream(), excelDataType, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }
}
