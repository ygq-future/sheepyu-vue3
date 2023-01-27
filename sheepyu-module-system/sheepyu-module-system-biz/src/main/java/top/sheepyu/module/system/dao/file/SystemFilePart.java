package top.sheepyu.module.system.dao.file;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ygq
 * @date 2023-01-25 15:29
 **/
@Data
@Accessors(chain = true)
public class SystemFilePart {
    private Long id;
    private Long fileId;
    private String path;
    private String md5;
    private byte[] partTag;
    private Integer idx;
    private Integer size;
}
