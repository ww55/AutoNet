package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * <p>
 * desc:setting the request MediaType
 * <p>
 * <p>
 * value: MediaType
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetMediaTypeAnontation {

    String value() default "application/json; charset=utf-8";

}
