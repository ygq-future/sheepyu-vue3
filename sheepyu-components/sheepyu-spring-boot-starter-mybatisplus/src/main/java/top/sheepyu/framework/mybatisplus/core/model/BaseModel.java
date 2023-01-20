package top.sheepyu.framework.mybatisplus.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-15 11:31
 **/
@Data
public class BaseModel implements Serializable {
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableLogic
    private Integer deleted;
}
