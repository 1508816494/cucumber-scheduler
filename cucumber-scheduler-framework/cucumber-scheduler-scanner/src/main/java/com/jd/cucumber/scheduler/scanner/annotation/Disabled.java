package com.jd.cucumber.scheduler.scanner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解可以用于类和方法,该注解将会让niubi-job忽略掉被注释的任务.
 * 如果该注解用在类上,那么该类中所有被{@link Schedule}注解的方法都会被忽略.
 * 如果该注解用在方法上,那么该方法哪怕被{@link Schedule}注解也将会被忽略.
 *
 * @see Schedule
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Disabled {
}
