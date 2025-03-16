package develop.whiskyNote.utils;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {
    String key();
    int waitTime() default 1;
    int leaseTime() default 5;
}
