package com.zhxh.xnetlib.annotation;

import com.zhxh.xnetlib.type.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //方法之上
@Retention(RetentionPolicy.RUNTIME) //运行时通过反射技术获取注解的值
public @interface Network {
    NetType netType() default NetType.AUTO;//默认 返回
}
