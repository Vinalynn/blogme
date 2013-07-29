package org.vinalynn.wapp.wmblog.util;

import com.google.appengine.api.datastore.*;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.DataBean;

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
                entity.setProperty(field.getName(),
                        get_GetValueMethod(obj.getClass(), field.getName()).invoke(obj));
            }
        }
        getDataStore().put(entity);
        return obj.getUuid();
    }

    public static List<DataBean> getDatas(String kind, Class dataClazz, Query.Filter[] filters) throws Exception {
        Query query = new Query(kind);
        PreparedQuery pq = getDataStore().prepare(query);
        List<DataBean> destArrays = new ArrayList<DataBean>();
        for (Entity entity : pq.asIterable()) {
            DataBean obj = (DataBean) dataClazz.newInstance();
            //set kind
            obj.setKind(kind);
            Field[] fields = dataClazz.getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (Field field : fields) {
                    get_SetValueMethod(field.getName(), obj, new Class<?>[]{field.getType()})
                            .invoke(obj, entity.getProperty(field.getName()));
                }
            }
            destArrays.add(obj);
        }
        return destArrays;
    }


    public static Method get_GetValueMethod(Class<?> clazz, String fieldName) throws Exception {
        return clazz.getDeclaredMethod(
                "get" + fieldName.replaceFirst(
                        fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()
                )
        );
    }

    private static Method get_SetValueMethod(String fieldName, Object obj, Class<?>[] paramsType) throws Exception {
        return obj.getClass().getDeclaredMethod(
                "set" + fieldName.replaceFirst(
                        fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()
                ),
                paramsType
        );
    }
}
