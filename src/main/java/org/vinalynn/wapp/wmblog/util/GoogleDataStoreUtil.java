package org.vinalynn.wapp.wmblog.util;

import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.annotations.GoogleStoreAction;
import org.vinalynn.wapp.wmblog.data.DataBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>利用Google DataStore实现的数据持久化工具，主要功能有：
 * <ul>
 * <li>1、JavaBean自助转换Entity，对于特殊字段实现注解解释功能。</li>
 * <li>2、支持分页查询数据，支持自定义排序。</li>
 * </ul>
 * </p>
 * User: caiwm
 * Date: 13-7-29
 * Time: 下午5:01
 */
public class GoogleDataStoreUtil {

    private static DatastoreService dataStore =
            DatastoreServiceFactory.getDatastoreService();

//    private static DatastoreService getDataStore() {
//        return DatastoreServiceFactory.getDatastoreService();
//    }

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

    public static Transaction beginTransaction(){
        TransactionOptions options = TransactionOptions.Builder.withXG(true);
        return dataStore.beginTransaction(options);
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
        //obj.setUuid(UUID.randomUUID().toString());
        //KeyRange kr = getDataStore().allocateIds(obj.getKind(), 1);
        //KeyFactory.createKey(obj.getKind(), UUID.randomUUID().toString());
        Entity entity;
        if (StringUtils.isNotEmpty(obj.getUuid())) {
            entity = new Entity(KeyFactory.createKey(obj.getKind(), obj.getUuid()));
        } else {
            entity = new Entity(KeyFactory.createKey(obj.getKind(), UUID.randomUUID().toString()));
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                //boolean storedAsPointedType = Boolean.FALSE;
                if (hasAnnotationOfPointedType(field, Text.class)) {
                    Text text = new Text(
                            String.valueOf(get_GetValueMethod(obj.getClass(),
                                    field.getName()).invoke(obj))
                    );
                    entity.setProperty(field.getName(), text);
                    //storedAsPointedType = Boolean.TRUE;
                } //else if(hasAnnotationOfNoSave(field)){
                //  continue;
                else {
                    entity.setProperty(field.getName(),
                            get_GetValueMethod(obj.getClass(), field.getName()).invoke(obj));
                }


//                Annotation[] annotations = field.getDeclaredAnnotations();
//                if (null != annotations && annotations.length > 0) {
//                    for (Annotation annotation : annotations) {
//                        if (annotation instanceof GoogleStoreAction) {
//                            GoogleStoreAction a = (GoogleStoreAction) annotation;
//                            if (StringUtils.equals(a.clazz().getName(), Text.class.getName())) {
//
//                            }
//                            storedAsPointedType = Boolean.TRUE;
//                        }
//                    }
//                }
                //if (!storedAsPointedType) {

                //}
            }
        }
        Field[] dataBeanFields = DataBean.class.getDeclaredFields();
        if (null != dataBeanFields && dataBeanFields.length > 0) {
            for (Field field : dataBeanFields) {
                if (!hasAnnotationOfNoSave(field)) {
                    entity.setProperty(field.getName(),
                            get_GetValueMethod(DataBean.class, field.getName()).invoke(obj));
                }
            }
        }
        dataStore.put(entity);
        // key's name is generated-uuid,
        // this is what will be returned.
        return entity.getKey().getName();
    }

    /**
     * @param query
     * @param page
     * @param pageSize
     * @param sortPName
     * @param sortType
     * @return
     * @throws Exception
     */
    public static List<Entity> executeQuery(Query query, int page, int pageSize,
                                            String sortPName, Query.SortDirection sortType,
                                            Query.Filter[] filters) throws Exception {
        if (null != filters) {
            query.setFilter(Query.CompositeFilterOperator.and(filters));
        }
        if (StringUtils.isNotEmpty(sortPName) && sortType != null) {
            query.addSort(sortPName, sortType);
        }
        PreparedQuery pq = dataStore.prepare(query);
        return pq.asList(FetchOptions.Builder.withOffset(page * pageSize).limit(pageSize));
    }

    /**
     * @param query
     * @param page
     * @param pageSize
     * @param sortPName
     * @param sortType
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> executeQuery(Query query, int page, int pageSize,
                                           String sortPName, Query.SortDirection sortType,
                                           Query.Filter[] filters, Class<T> clazz) throws Exception {
        List<Entity> eResults = executeQuery(query, page, pageSize, sortPName, sortType, filters);
        List<T> tResults = null;
        if (null != eResults && eResults.size() > 0) {
            tResults = new ArrayList<T>();
            for (Entity entity : eResults) {
                tResults.add(makeUpBean(clazz, entity));
            }
        }
        return tResults;
    }

    /**
     * @param key
     * @param dataClazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getDataByKey(Key key, Class<T> dataClazz) throws Exception {
        Entity entity = dataStore.get(key);
        return makeUpBean(dataClazz, entity);
    }

    /**
     * @param clazz
     * @param entity
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T makeUpBean(Class<T> clazz, Entity entity) throws Exception {

        Field[] fields = clazz.getDeclaredFields();
        if (null == fields || fields.length < 1) {
            return null;
        }
        if (null == entity) return null;

        T obj = clazz.newInstance();

        ((DataBean) obj).setKind(entity.getKind());
        for (Field field : fields) {
            if (null == entity.getProperty(field.getName()))
                continue;
            if (hasAnnotationOfPointedType(field, Text.class)) {
                Object e_obj = entity.getProperty(field.getName());
                get_SetValueMethod(field.getName(), clazz, new Class<?>[]{field.getType()})
                        .invoke(obj, ((Text) e_obj).getValue());
            } else {
                get_SetValueMethod(field.getName(), clazz, new Class<?>[]{field.getType()})
                        .invoke(obj, entity.getProperty(field.getName()));
            }
        }

        fields = DataBean.class.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                if (hasAnnotationOfPointedType(field, Text.class)) {
                    Object e_obj = entity.getProperty(field.getName());
                    get_SetValueMethod(field.getName(), DataBean.class, new Class<?>[]{field.getType()})
                            .invoke(obj, ((Text) e_obj).getValue());
                } else if (hasAnnotationOfNoSave(field)) {
                    //把UUID从Entity的主键中读取出来，设置到DataBean的uuid成员变量中
                    if (StringUtils.equals("uuid", field.getName())) {
                        ((DataBean) obj).setUuid(entity.getKey().getName());
                    }
                } else {
                    get_SetValueMethod(field.getName(), DataBean.class, new Class<?>[]{field.getType()})
                            .invoke(obj, entity.getProperty(field.getName()));
                }
            }
        }
        return obj;
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
        PreparedQuery pq = dataStore.prepare(query);
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
                    } else if (hasAnnotationOfNoSave(field)) {
                        //把UUID从Entity的主键中读取出来，设置到DataBean的uuid成员变量中
                        if (StringUtils.equals("uuid", field.getName())) {
                            ((DataBean) obj).setUuid(entity.getKey().getName());
                        }
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
     * <p><code>GoogleStoreAction</code>是自定义的Annotation，for detail,
     * you might click this
     * {@link org.vinalynn.wapp.wmblog.annotations.GoogleStoreAction}</p>
     *
     * @param f            Any Field of a <code>Object</code>
     * @param pointedClass <code>Class&lt;? pointedClass&gt;</code>,
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
            if (annotation instanceof GoogleStoreAction) {
                GoogleStoreAction gst = (GoogleStoreAction) annotation;
                if (StringUtils.equals(gst.storeType().getName(), pointedClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAnnotationOfNoSave(Field f) throws Exception {
        if (null == f) return Boolean.FALSE;
        Annotation[] annotations = f.getDeclaredAnnotations();
        if (null == annotations || annotations.length < 1) {
            return Boolean.FALSE;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof GoogleStoreAction) {
                GoogleStoreAction gst = (GoogleStoreAction) annotation;
                if (BooleanUtils.isTrue(gst.noSave())) {
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
