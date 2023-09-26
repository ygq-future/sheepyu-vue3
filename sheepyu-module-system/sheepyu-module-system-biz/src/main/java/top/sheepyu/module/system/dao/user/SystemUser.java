package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.Date;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-18 14:36
 **/
@TableName(autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemUser extends BaseModel {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String mobile;
    private String avatar;
    /**
     * 部门/职位id
     */
    @TableField(exist = false)
    private Set<Long> deptIds;
    /**
     * 部门和职位名称
     */
    @TableField(exist = false)
    private String deptNames;
    private Integer status;
    private Integer type;
    private String remark;
    private String loginIp;
    private Date loginTime;
}
