package org.vinalynn.wapp.wmblog.data;

/**
 * 实体关系
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午2:59
 */
public class EntityRelationBean extends DataBean {
    public String entityA;
    public String entityB;
    public String relationType;
    public String state;

    public String getEntityA() {
        return entityA;
    }

    public void setEntityA(String entityA) {
        this.entityA = entityA;
    }

    public String getEntityB() {
        return entityB;
    }

    public void setEntityB(String entityB) {
        this.entityB = entityB;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
