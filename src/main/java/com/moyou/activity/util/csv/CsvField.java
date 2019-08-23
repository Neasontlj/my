package com.moyou.activity.util.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wyf 2018/11/5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {

    String name() default "";

    int maxLen() default 0;

    int minLen() default 0;

    boolean isNotNull() default false;

    String regex() default "";

}
