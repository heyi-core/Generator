package com.heyi.${project.dataBase}.baseannotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeat {
    String desc()  default "无动作";
}