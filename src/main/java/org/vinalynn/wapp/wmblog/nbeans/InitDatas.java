package org.vinalynn.wapp.wmblog.nbeans;

import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

/**
 * User: caiwm
 * Date: 13-8-4
 * Time: 下午12:29
 */
public class InitDatas {
    private List<String> configDatas;
    private List<String> anotherDatas;

    public List<String> getConfigDatas() {
        return configDatas;
    }

    public void setConfigDatas(List<String> configDatas) {
        this.configDatas = configDatas;
    }

    public List<String> getAnotherDatas() {
        return anotherDatas;
    }

    public void setAnotherDatas(List<String> anotherDatas) {
        this.anotherDatas = anotherDatas;
    }

    public static void main(String[] args) throws Exception{
//        InitDatas ids = new InitDatas();
//        List<String> lds = new ArrayList<String>();
//        lds.add("school");
//        lds.add("high school");
//        ids.setConfigDatas(lds);
//
//        List<String> alds = new ArrayList<String>();
//        alds.add("----------------");
//        ids.setAnotherDatas(alds);
//
        XStream xStream = new XStream();
        xStream.alias("InitDatas", InitDatas.class);
//        System.out.println(xStream.toXML(ids));


        String configDataPath = "/system/init-data.xml";
        Object obj = xStream.fromXML(InitDatas.class.getResourceAsStream(configDataPath));
        System.out.println(obj);
    }
}
