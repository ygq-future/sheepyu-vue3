package top.sheepyu.module.common.annotations;

import top.sheepyu.module.common.common.IterableEnum;
import top.sheepyu.module.common.validator.InEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author ygq
 * @date 2022-12-29 16:34
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = InEnumValidator.class)
public @interface InEnum {
    Class<? extends IterableEnum> value();

    String message() default "参数不在可接受列表内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
