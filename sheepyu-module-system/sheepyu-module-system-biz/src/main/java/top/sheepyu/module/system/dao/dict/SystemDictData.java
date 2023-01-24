package top.sheepyu.module.system.dao.dict;

import lombok.Data;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.Objects;

/**
 * @author ygq
 * @date 2023-01-24 13:43
 **/
@Data
@Accessors(chain = true)
public class SystemDictData extends BaseModel {
    private Long id;
    private Long dictTypeId;
    private Integer sort;
    private String label;
    private String value;
    private Integer visible;
    private String component;
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemDictData dictData = (SystemDictData) o;
        return dictTypeId.equals(dictData.dictTypeId) && label.equals(dictData.label) && value.equals(dictData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dictTypeId, label, value);
    }
}
