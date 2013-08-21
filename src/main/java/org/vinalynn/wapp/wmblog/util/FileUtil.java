package org.vinalynn.wapp.wmblog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * User: caiwm
 * Date: 13-8-4
 * Time: 下午2:26
 */
public class FileUtil {

    /**
     *
     * @param uri
     * @return
     * @throws Exception
     */
    public static String readFile(URI uri, String encoding) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(uri)), encoding
        ));
        StringBuilder sb = new StringBuilder();
        String tmp;
        while( (tmp = br.readLine()) != null){
            while(tmp.startsWith(" ")){
                tmp = tmp.substring(1, tmp.length());
            }
            sb.append(tmp);
        }
        br.close();
        return sb.toString();
    }
}
