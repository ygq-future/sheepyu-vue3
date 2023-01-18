package top.sheepyu.framework.security.core.annotations;

import java.lang.annotation.*;

/**
 * @author ygq
 * @date 2022-12-03 16:34
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permit {
}
