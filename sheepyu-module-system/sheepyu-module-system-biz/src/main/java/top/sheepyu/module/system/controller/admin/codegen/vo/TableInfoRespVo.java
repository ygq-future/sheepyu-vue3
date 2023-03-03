package top.sheepyu.module.system.controller.admin.codegen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ygq
 * @date 2023-03-03 19:46
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfoRespVo {
    private String name;
    private String comment;
}
