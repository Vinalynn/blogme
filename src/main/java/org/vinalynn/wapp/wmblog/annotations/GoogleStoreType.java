package org.vinalynn.wapp.wmblog.annotations;

import com.google.appengine.api.datastore.Text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>因为Google DataStore的Entity对于属性的大小要求比较严格，
 * 所以对于比较大的String采用DataStore的Text来存取，所以在存取
 * 的时候要多封装一层。这次采用注解的方式来生命需要转换成Text的字
 * 段。</p>
 * User: caiwm
 * Date: 13-7-30
 * Time: 上午10:29
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoogleStoreType {
    //
    Class<?> clazz() default Text.class;
}
