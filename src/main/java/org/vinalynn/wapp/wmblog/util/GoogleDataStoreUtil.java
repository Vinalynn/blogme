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
 * Time: ����5:01
 */
public class GoogleDataStoreUtil {

    private static DatastoreService getDataStore() {
        return DatastoreServiceFactory.getDatastoreService();
    }

    /**
     * <p>��Javabean�ĸ���field����Entity��Ȼ�󱣴浽Google��DataStore�С�
     * ������UUID</p>
     *
     * @param obj
     * @throws Exception
     */
    public static void storeSingleBean(DataBean obj) throws Exception {
        storeSingleDataWithUUIDRtn(obj);
    }

    /**
     * <p>��Javabean�ĸ���field����Entity��Ȼ�󱣴浽Google��DataStore�С�
     * �������ɵ�uuid</p>
     *
     * @param obj Ҫ�����JavaBean
     * @return Data���ݵ�Ψһ��ʶ uuid
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
     * ��ȡ���ݣ����ݲ�ͬ��Kind����Լ�ҵ���������
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
     * <p>��Ϊ�ڴ�Google DataStore��ȡ����ʱ�������DataStore�в��ǻ������ͣ�
     * ͨ����������ᷢ����java.lang.String���͵������С����ʱ����Ҫ��Text����
     * �Ĵ洢����ת����String���ͣ�����JavaBean�С������������Ϊ���ж�JavaBean
     * ��ĳ��Field�Ƿ���ָ���Ĵ洢����ע�⡣����У���ǿת�ɶ�Ӧ�����ͣ�������
     * Google�ṩ��API��������ȡ��String���ڷ�����ͨ��JavaBean�С�</p>
     * <p/>
     * <p><code>GoogleStoreType</code>���Զ����Annotation��for detail,
     * you might click this{@link GoogleStoreType}</p>
     *
     * @param f            Any Field of a <code>Object</code>
     * @param pointedClass <code>Class<?> pointedClass</?></code>,
     *                     Google DataStore�Ĵ洢���ͣ�һ��ʹ��<code>Text
     *                     </code>�ıȽ϶ࡣ
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
     * <p>get the getter of a field.һ����ͨ��JavaBean�������ɳ�Ա����
     * ��getter��setter.��Ϊgetterһ����û�в����ģ�����ֱ��ʹ����Class����������
     * ������ȡ������</p>
     *
     * @param clazz     field������class
     * @param fieldName Field�ֶ�����
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
     * <p>���ݳ�Ա���������ͣ���ȡClass�������Ա������setter.ֱ�ӻ�ȡ��Ա����
     * ��������Ϊ�����Ϳ����ˡ�</p>
     *
     * @param fieldName  ��Ա������ֵ
     * @param clazz      ��Ա����������Bean
     * @param paramsType ��Ա���������ͣ������ʵ����Ҫ�ġ�
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
