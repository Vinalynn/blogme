package org.vinalynn.wapp.wmblog.util;

import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.annotations.GoogleStoreType;
import org.vinalynn.wapp.wmblog.data.DataBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: caiwm
 * Date: 13-7-29
 * Time: 下午5:01
 */
public class GoogleDataStoreUtil {

    private static DatastoreService getDataStore() {
        return DatastoreServiceFactory.getDatastoreService();
    }

    /**
     * <p>将Javabean的各项field存入Entity，然后保存到Google的DataStore中。
     * 不返回UUID</p>
     *
     * @param obj
     * @throws Exception
     */
    public static void storeSingleBean(DataBean obj) throws Exception {
        storeSingleDataWithUUIDRtn(obj);
    }

    /**
     * <p>将Javabean的各项field存入Entity，然后保存到Google的DataStore中。
     * 返回生成的uuid</p>
     *
     * @param obj 要保存的JavaBean
     * @return Data数据的唯一标识 uuid
     * @throws Exception
     */
    public static String storeSingleDataWithUUIDRtn(DataBean obj) throws Exception {
        if (null == obj) {
            throw new Exception(GlobalConst.B_EXCEPTION.INVALID_PARAMS);
        }
        obj.setUuid(UUID.randomUUID().toString());
        KeyRange kr = getDataStore().allocateIds(obj.getKind(), 1);
        Entity entity = new Entity(kr.getStart());

        Field[] fields = obj.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                boolean storedAsPointedType = Boolean.FALSE;
                if (hasAnnotationOfPointedType(field, Text.class)) {
                    Text text = new Text(
                            String.valueOf(get_GetValueMethod(obj.getClass(),
                                    field.getName()).invoke(obj))
                    );
                    entity.setProperty(field.getName(), text);
                    storedAsPointedType = Boolean.TRUE;
                }

//                Annotation[] annotations = field.getDeclaredAnnotations();
//                if (null != annotations && annotations.length > 0) {
//                    for (Annotation annotation : annotations) {
//                        if (annotation instanceof GoogleStoreType) {
//                            GoogleStoreType a = (GoogleStoreType) annotation;
//                            if (StringUtils.equals(a.clazz().getName(), Text.class.getName())) {
//
//                            }
//                            storedAsPointedType = Boolean.TRUE;
//                        }
//                    }
//                }
                if (!storedAsPointedType) {
                    entity.setProperty(field.getName(),
                            get_GetValueMethod(obj.getClass(), field.getName()).invoke(obj));
                }
            }
        }
        Field[] dataBeanFields = DataBean.class.getDeclaredFields();
        if (null != dataBeanFields && dataBeanFields.length > 0) {
            for (Field field : dataBeanFields) {
                entity.setProperty(field.getName(),
                        get_GetValueMethod(DataBean.class, field.getName()).invoke(obj));
            }
        }

        getDataStore().put(entity);
        return obj.getUuid();
    }

    /**
     * 获取数据，根据不同的Kind类别，以及业务对象类型
     *
     * @param kind
     * @param dataClazz
     * @param filters
     * @return
     * @throws Exception
     */
    public static <T> List<T> getDatas(String kind, Class<T> dataClazz, Query.Filter[] filters) throws Exception {

        Query query = new Query(kind);
        if (null != filters && filters.length > 0) {
            query.setFilter(
                    Query.CompositeFilterOperator.and(filters)
            );
        }
        PreparedQuery pq = getDataStore().prepare(query);
        List<T> destArrays = new ArrayList<T>();
        for (Entity entity : pq.asIterable()) {
            T obj = dataClazz.newInstance();
            //set kind
            ((DataBean) obj).setKind(kind);
            Field[] fields = dataClazz.getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (Field field : fields) {
                    if (hasAnnotationOfPointedType(field, Text.class)) {
                        Object e_obj = entity.getProperty(field.getName());
                        get_SetValueMethod(field.getName(), dataClazz, new Class<?>[]{field.getType()})
                                .invoke(obj, ((Text) e_obj).getValue());
                    } else {
                        get_SetValueMethod(field.getName(), dataClazz, new Class<?>[]{field.getType()})
                                .invoke(obj, entity.getProperty(field.getName()));
                    }
                }
            }
            Field[] fields2 = DataBean.class.getDeclaredFields();
            if (null != fields2 && fields2.length > 0) {
                for (Field field : fields2) {
                    if (hasAnnotationOfPointedType(field, Text.class)) {
                        Object e_obj = entity.getProperty(field.getName());
                        get_SetValueMethod(field.getName(), DataBean.class, new Class<?>[]{field.getType()})
                                .invoke(obj, ((Text) e_obj).getValue());
                    } else {
                        get_SetValueMethod(field.getName(), DataBean.class, new Class<?>[]{field.getType()})
                                .invoke(obj, entity.getProperty(field.getName()));
                    }
                }
            }

            destArrays.add(obj);
        }
        return destArrays;
    }

    /**
     * <p>因为在从Google DataStore读取数据时，如果在DataStore中不是基础类型，
     * 通常这种情况会发生在java.lang.String类型的数据中。这个时候需要将Text类型
     * 的存储数据转化成String类型，存入JavaBean中。这个方法就是为了判断JavaBean
     * 的某个Field是否含有指定的存储类型注解。如果有，则强转成对应的类型，再利用
     * Google提供的API将数据提取成String，在放入普通的JavaBean中。</p>
     * <p/>
     * <p><code>GoogleStoreType</code>是自定义的Annotation，for detail,
     * you might click this{@link GoogleStoreType}</p>
     *
     * @param f            Any Field of a <code>Object</code>
     * @param pointedClass <code>Class<?> pointedClass</?></code>,
     *                     Google DataStore的存储类型，一般使用<code>Text
     *                     </code>的比较多。
     * @return if f has the annotation of pointed store type 'pointedClass'
     *         return true, then return false.
     * @throws Exception
     */
    public static boolean hasAnnotationOfPointedType(Field f, Class<?> pointedClass) throws Exception {
        if (null == f) return Boolean.FALSE;
        Annotation[] annotations = f.getDeclaredAnnotations();
        if (null == annotations || annotations.length < 1) {
            return Boolean.FALSE;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof GoogleStoreType) {
                GoogleStoreType gst = (GoogleStoreType) annotation;
                if (StringUtils.equals(gst.clazz().getName(), pointedClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>get the getter of a field.一般普通的JavaBean都会生成成员变量
     * 的getter和setter.因为getter一般是没有参数的，所以直接使用了Class不带参数的
     * 方法获取函数。</p>
     *
     * @param clazz     field归属的class
     * @param fieldName Field字段名称
     * @return Method of getFieldName
     * @throws Exception
     */
    public static Method get_GetValueMethod(Class<?> clazz, String fieldName) throws Exception {
        return clazz.getDeclaredMethod(
                "get" + fieldName.replaceFirst(
                        fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()
                )
        );
    }

    /**
     * <p>根据成员变量的类型，获取Class中这个成员变量的setter.直接获取成员变量
     * 的类型作为参数就可以了。</p>
     *
     * @param fieldName  成员变量的值
     * @param clazz      成员变量归属的Bean
     * @param paramsType 成员变量的类型，这个其实很重要的。
     * @return
     * @throws Exception
     */
    private static Method get_SetValueMethod(String fieldName, Class<?> clazz, Class<?>[] paramsType) throws Exception {
        return clazz.getDeclaredMethod(
                "set" + fieldName.replaceFirst(
                        fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()
                ),
                paramsType
        );
    }
}
