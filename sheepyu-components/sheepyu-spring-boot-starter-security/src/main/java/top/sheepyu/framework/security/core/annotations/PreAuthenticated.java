package top.sheepyu.framework.security.core.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthenticated {
}
