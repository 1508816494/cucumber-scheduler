
package com.jd.cucumber.scheduler.core.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpHelper {

    /**
     * 下载远程资源
     *
     * @param filePath 要保存的文件路径
     * @param url 要下载的资源URL
     * @return 返回下载后保存的文件路径
     * @throws IOException 如果下载过程出现IO异常则抛出
     */
    static String downloadRemoteResource(String filePath, String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        byte[] bytes = IOHelper.readStreamBytesAndClose(connection.getInputStream());
        IOHelper.writeFile(filePath, bytes);
        return filePath;
    }

}
