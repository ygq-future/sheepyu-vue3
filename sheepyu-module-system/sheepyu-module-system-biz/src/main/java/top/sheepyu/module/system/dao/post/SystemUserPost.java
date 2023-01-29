package top.sheepyu.module.system.dao.post;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-29 16:13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemUserPost extends BaseModel {
    private Long id;
    private Long userId;
    private Long postId;
}
