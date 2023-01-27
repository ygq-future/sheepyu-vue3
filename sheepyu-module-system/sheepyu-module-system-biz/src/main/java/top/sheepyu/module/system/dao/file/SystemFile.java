package top.sheepyu.module.system.dao.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-25 16:09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemFile extends BaseModel {
    private Long id;
    private String filename;
    private String md5;
    private String url;
    private String mimeType;
    private Integer size;
    private String domain;
    private String path;
    private Integer complete;
    private String remark;
}
