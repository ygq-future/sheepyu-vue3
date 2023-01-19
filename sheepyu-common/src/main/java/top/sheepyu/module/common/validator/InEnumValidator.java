package top.sheepyu.module.common.validator;

import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.IterableEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;

/**
 * @author ygq
 * @date 2022-12-29 16:36
 **/
public class InEnumValidator implements ConstraintValidator<InEnum, Integer> {
    private List<Integer> list;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return list.contains(value);
    }

    @Override
    public void initialize(InEnum inEnum) {
        IterableEnum[] values = inEnum.value().getEnumConstants();
        if (values.length == 0) {
            this.list = Collections.emptyList();
        } else {
            this.list = values[0].list();
        }
    }
}
