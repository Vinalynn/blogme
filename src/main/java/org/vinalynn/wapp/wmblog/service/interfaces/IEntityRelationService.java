package org.vinalynn.wapp.wmblog.service.interfaces;

import java.util.HashMap;

/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午4:09
 */
public interface IEntityRelationService {

    /**
     *
     * @param kv
     * @param relationType
     * @throws Exception
     */
    public void createEntityRelation(HashMap<String, String> kv, String relationType) throws Exception;


}
