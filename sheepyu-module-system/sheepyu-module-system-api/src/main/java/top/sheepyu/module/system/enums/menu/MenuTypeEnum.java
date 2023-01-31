package top.sheepyu.module.system.enums.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-30 11:09
 **/
@AllArgsConstructor
@Getter
public enum MenuTypeEnum implements IterableEnum {
    CATALOG(1), //目录
    MENU(2),    //菜单
    BUTTON(3),  //按钮
    ;
    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(MenuTypeEnum::getCode).collect(Collectors.toList());
    }
}
