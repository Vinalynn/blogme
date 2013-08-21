package org.vinalynn.wapp.wmblog.service.impl;

import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang3.StringUtils;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.EntityRelationBean;
import org.vinalynn.wapp.wmblog.data.TagBean;
import org.vinalynn.wapp.wmblog.service.interfaces.IEntityRelationService;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午4:09
 */
public class EntityRelationServiceImpl implements IEntityRelationService {

    @Override
    public void createEntityRelation(HashMap<String, String> kv, String relationType) throws Exception {
        if (null == kv || kv.size() < 1) return;
        Iterator<Map.Entry<String, String>> iterator = kv.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!isEntityRelationExist(entry.getKey(), entry.getValue(),
                    relationType, GlobalConst.STATE_VALID)
                    ) {
                  createEntityRelation(entry.getKey(),entry.getValue(), relationType);
            }
        }
    }

    private void createEntityRelation(String ea, String eb, String relationType) throws Exception {
        EntityRelationBean bean = new EntityRelationBean();
        bean.setState(GlobalConst.STATE_VALID);
        bean.setKind(GlobalConst.KIND_ENTITY_RELATION);
        bean.setEntityA(ea);
        bean.setEntityB(eb);
        bean.setRelationType(relationType);
        GoogleDataStoreUtil.storeSingleBean(bean);
    }

    /**
     *
     * @param eaId
     * @param ebId
     * @param relationType
     * @param state
     * @return
     * @throws Exception
     */
    private boolean isEntityRelationExist(String eaId,
                                          String ebId, String relationType, String state) throws Exception{
        EntityRelationBean bean = getTagBean(eaId, ebId, relationType, state);
        return null == bean ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     *
     * @param eaId
     * @param ebId
     * @param relationType
     * @param state
     * @return
     * @throws Exception
     */
    private EntityRelationBean getTagBean(String eaId, String ebId, String relationType, String state) throws Exception {
        if (StringUtils.isEmpty(state)) state = GlobalConst.STATE_VALID;
        Query.Filter eaIdFilter = new Query.FilterPredicate(
                "entityA",
                Query.FilterOperator.EQUAL,
                eaId
        );
        Query.Filter ebIdFilter = new Query.FilterPredicate(
                "entityB",
                Query.FilterOperator.EQUAL,
                ebId
        );
        Query.Filter rTypeFilter = new Query.FilterPredicate(
                "relationType",
                Query.FilterOperator.EQUAL,
                relationType
        );
        Query.Filter stateFilter = new Query.FilterPredicate(
                "state",
                Query.FilterOperator.EQUAL,
                state
        );

        List<EntityRelationBean> entityRelationBeanList = GoogleDataStoreUtil.getDatas(
                GlobalConst.KIND_ENTITY_RELATION,
                EntityRelationBean.class, new Query.Filter[]{
                eaIdFilter, ebIdFilter, rTypeFilter, stateFilter
        });
        if (null != entityRelationBeanList && entityRelationBeanList.size() > 0) {
            return entityRelationBeanList.get(0);
        }
        return null;
    }
}
