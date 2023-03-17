package top.sheepyu.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 17:04
 **/
@AllArgsConstructor
@Getter
public enum FileUploadCompleteEnum implements IterableEnum {
    COMPLETE(1), //完成
    INCOMPLETE(0), //未完成
    ;
    private final int code;

    @Override
    public List<Integer> list() {
        return ArrayUtil.map(values(), FileUploadCompleteEnum::getCode);
    }
}
