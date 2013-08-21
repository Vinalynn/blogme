package org.vinalynn.wapp.wmblog.service.impl;

import com.google.appengine.api.datastore.Query;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vinalynn.wapp.wmblog.data.ConfigData;
import org.vinalynn.wapp.wmblog.service.interfaces.IDataInitService;
import org.vinalynn.wapp.wmblog.util.FileUtil;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: caiwm
 * Date: 13-8-4
 * Time: 下午12:56
 */
public class DataInitServiceImpl implements IDataInitService {
    private transient static Logger log = Logger.getLogger(DataInitServiceImpl.class);

    static {
        try {
            initData();

        } catch (Exception e) {
            log.error("ERROR-------------configuration data init failed.");
            e.printStackTrace();
        }
    }

    private static void initData() throws Exception {
         String configDataXml =
                FileUtil.readFile(
                        ConfigData.class.getResource("/system/init-data.xml").toURI(),
                        "UTF-8"
                );

        log.error(configDataXml);
        Document doc = DocumentHelper.parseText(configDataXml);
        String xpath = "/InitDatas/configDatas/string" ;

        List<Element> lists = doc.selectNodes(xpath);
        if(null != lists && lists.size() > 0){
            List<String> cds = new ArrayList<String>();
            for(Element element : lists){
                cds.add(element.getText());
            }
            if(cds.size() > 0){
                initConfigDatas(cds);
            }
        }
    }

    private static void initConfigDatas(List<String> cds) throws Exception {
        if (null == cds || cds.size() < 1) {
            return;
        }

        for (String s : cds) {
            String[] strings = s.split(Pattern.quote("$|$"));

            Query.Filter configKeyFilter = new Query.FilterPredicate(
                    "configKey", Query.FilterOperator.EQUAL, strings[1]);
            Query.Filter stateFilter = new Query.FilterPredicate(
                    "state", Query.FilterOperator.EQUAL, strings[3]
            );

            List<ConfigData> configDatas = GoogleDataStoreUtil.getDatas(strings[0], ConfigData.class,
                    new Query.Filter[]{configKeyFilter, stateFilter});
            if (null != configDatas && configDatas.size() > 0) {
                ConfigData cd = configDatas.get(0);
                //cd.setConfigKey(strings[1]);
                cd.setConfigValue(strings[2]);
                cd.setState(strings[3]);
                String uuid = GoogleDataStoreUtil.storeSingleDataWithUUIDRtn(cd);
                if (log.isInfoEnabled()) {
                    log.info("uuid:[" + uuid + "],configKey:[" + cd.getConfigKey() + "] updated.");
                }
            } else {
                ConfigData cd = new ConfigData();
                cd.setKind(strings[0]);
                cd.setConfigKey(strings[1]);
                cd.setConfigValue(strings[2]);
                cd.setState(strings[3]);
                String uuid = GoogleDataStoreUtil.storeSingleDataWithUUIDRtn(cd);
                if (log.isInfoEnabled()) {
                    log.info("uuid:[" + uuid + "],configKey:[" + cd.getConfigKey() + "] inserted.");
                }
            }

        }

    }

}
