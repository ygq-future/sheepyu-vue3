package top.sheepyu.module.common.util;

import cn.hutool.core.collection.CollUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

public class ValidateUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验集合
     */
    public static <T> void validate(Collection<T> collection) {
        collection.forEach(ValidateUtil::validate);
    }

    /**
     * 校验实体类
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (CollUtil.isNotEmpty(constraintViolations)) {
            StringBuilder validateError = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError.append(constraintViolation.getMessage()).append(";");
            }
            throw new ValidationException(validateError.toString());
        }
    }

    /**
     * 通过组来校验实体类
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);
        if (CollUtil.isNotEmpty(constraintViolations)) {
            StringBuilder validateError = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError.append(constraintViolation.getMessage()).append(";");
            }

            throw new ValidationException(validateError.toString());
        }
    }
}
