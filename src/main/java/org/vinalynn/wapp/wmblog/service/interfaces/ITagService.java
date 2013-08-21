package org.vinalynn.wapp.wmblog.service.interfaces;

import java.util.HashMap;

/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 下午3:28
 */
public interface ITagService {

    /**
     * 解析标签名称字符串。这些字符串是用逗号分隔的。
     * 返回的结果是HashMap, Key是tag的名称，value
     * 是Tag的UUID
     *
     * @param tags 用逗号分隔的标签名称字符串
     * @throws Exception
     */
    public HashMap<String, String> parseTags(String tags) throws Exception;

    /**
     * 创建标签，返回标签的UUID
     *
     * @param tagName 标签的名字
     * @return 标签的UUID
     * @throws Exception
     */
    public String createTag(String tagName) throws Exception;
}
