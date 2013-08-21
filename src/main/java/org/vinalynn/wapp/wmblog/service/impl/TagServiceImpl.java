package org.vinalynn.wapp.wmblog.service.impl;

import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang3.StringUtils;
import org.vinalynn.wapp.wmblog.GlobalConst;
import org.vinalynn.wapp.wmblog.data.TagBean;
import org.vinalynn.wapp.wmblog.service.interfaces.ITagService;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午3:28
 */
public class TagServiceImpl implements ITagService {

    public HashMap<String, String> parseTags(String tags) throws Exception {

        HashMap<String, String> tagR = new HashMap<String, String>();
        if (StringUtils.isEmpty(tags)) return tagR;
        //根据tag的名称找Tag记录。

        String[] tagSplits = tags.split(Pattern.quote(","));
        if (null == tagSplits || tagSplits.length < 1) return tagR;


        for (String tag : tagSplits) {
            //先不考虑效率问题，一个个找吧
            TagBean tb = getTagsByName(tag, StringUtils.EMPTY);
            if (null != tb) {
                tagR.put(tag, tb.getUuid());
            } else {
                //如果Tag尚未创建，则新建Tag
                tagR.put(tag, createTag(tag));
            }
        }
        return tagR;
    }

    /**
     * 根据tag的名字查询Tag，正常情况下有效的Tag只有一个。
     * 如果查询处理的数据有多条，则返回第一条数据。
     * state如果不传默认查询有效的数据。
     *
     * @param tagName 标签名称
     * @param state   标签状态
     * @return TagBean
     * @throws Exception
     */
    private TagBean getTagsByName(String tagName, String state) throws Exception {
        if (StringUtils.isEmpty(state)) state = GlobalConst.STATE_VALID;

        Query.Filter stateFilter = new Query.FilterPredicate(
                "state",
                Query.FilterOperator.EQUAL,
                GlobalConst.STATE_VALID
        );
        Query.Filter tagNameFilter = new Query.FilterPredicate(
                "tagName",
                Query.FilterOperator.EQUAL,
                tagName
        );
        List<TagBean> tagBeans = GoogleDataStoreUtil.getDatas(GlobalConst.KIND_TAG,
                TagBean.class, new Query.Filter[]{stateFilter, tagNameFilter});
        if (null != tagBeans && tagBeans.size() > 0) {
            return tagBeans.get(0);
        }
        return null;
    }

    @Override
    public String createTag(String tagName) throws Exception {
        TagBean tagBean = new TagBean();
        tagBean.setKind(GlobalConst.KIND_TAG);
        tagBean.setState(GlobalConst.STATE_VALID);
        tagBean.setTagName(tagName);
        return GoogleDataStoreUtil.storeSingleDataWithUUIDRtn(tagBean);
    }
}
