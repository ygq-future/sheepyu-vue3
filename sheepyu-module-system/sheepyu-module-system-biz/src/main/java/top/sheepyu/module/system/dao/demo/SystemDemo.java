package top.sheepyu.module.system.dao.demo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;
import java.util.*;

/**
* @author sheepyu
* @date 2023-03-10 21:40:22
**/
@TableName("system_demo")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemDemo extends BaseModel {
    /**
    * id
    */
    private Long id;
    /**
    * 测试名称
    */
    private String name;
    /**
    * 测试内容
    */
    private String content;
    /**
    * 启用状态
    * 字典: common_status
    */
    private Integer status;
    /**
    * 开始时间
    */
    private Date beginTime;
    /**
    * 年龄
    */
    private Integer age;
    /**
    * 性别
    * 字典: common_boolean_status
    */
    private Integer sex;
}
