package org.vinalynn.wapp.wmblog.annotations;

import com.google.appengine.api.datastore.Text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>��ΪGoogle DataStore��Entity�������ԵĴ�СҪ��Ƚ��ϸ�
 * ���Զ��ڱȽϴ��String����DataStore��Text����ȡ�������ڴ�ȡ
 * ��ʱ��Ҫ���װһ�㡣��β���ע��ķ�ʽ��������Ҫת����Text����
 * �Ρ�</p>
 * User: caiwm
 * Date: 13-7-30
 * Time: ����10:29
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoogleStoreType {
    //
    Class<?> clazz() default Text.class;
}
