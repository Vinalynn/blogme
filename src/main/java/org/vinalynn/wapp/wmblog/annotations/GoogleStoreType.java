package org.vinalynn.wapp.wmblog.annotations;

import com.google.appengine.api.datastore.Text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: caiwm
 * Date: 13-7-30
 * Time: ионГ10:29
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoogleStoreType {
    //
    Class<?> clazz() default Text.class;
}
