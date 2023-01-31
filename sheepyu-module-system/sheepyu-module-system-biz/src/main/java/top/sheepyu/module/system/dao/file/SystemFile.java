package top.sheepyu.module.system.dao.file;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 系统文件不使用逻辑删除, 因为没有软用
 *
 * @author ygq
 * @date 2023-01-25 16:09
 **/
@Data
@Accessors(chain = true)
public class SystemFile {
    private Long id;
    private String uploadId;
    private Integer partIndex;
    private String filename;
    private String md5;
    private String url;
    private String mimeType;
    private Long size;
    private String domain;
    private String path;
    private Integer complete;
    private String remark;
    private String creator;
    private Date createTime;
}
