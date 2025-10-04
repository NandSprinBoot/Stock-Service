package com.stock.generator;

import org.hibernate.annotations.IdGeneratorType;
import java.lang.annotation.*;

@IdGeneratorType(PrefixSequenceIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PrefixSequenceGenerator {
    String prefix() default "GEN";
    String sequence() default "global_seq";
    int initial_Value() default 1;
    int incrementSize() default 1;
}