package org.vinalynn.wapp.wmblog;

/**
 * User: caiwm
 * Date: 13-7-26
 * Time: AM 1:48
 */
public class GlobalConst {

    public static final String FTL_COMMON_INDEX = "index";
    public static final String FTL_ARTICLE_POSTER = "article-poster";
    public static final String FTL_COMMON_MSG_URL = "common/msg";
    public static final String FTL_COMMON_MSG_KEY = "msg";

    //------------------------------------------------------------

    public static final String KIND_ARTICLE = "Article";
    public static final String KIND_COMMENTS = "Comments";
    public static final String KIND_CONFIGDATA = "ConfigData";
    public static final String KIND_TAG = "Tag";
    public static final String KIND_ENTITY_RELATION = "EntityRelation";

    //------------------------------------------------------------
    public static final String FILTER_UUID = "uuid";

   //--------------------------------------------------------------
    public static final String KEY_BLOG_INFO = "bi";

    //-----------------------------------------------------
    public static final String STATE_VALID = "U";
    //-------------------------------------------------------
    public static final String RELATION_ARTICLE_TAG = "RELATION_ARTICLE_TAG";

    public static interface B_EXCEPTION {
        public static final String INVALID_PARAMS = "invalid params exception.";
    }
}
