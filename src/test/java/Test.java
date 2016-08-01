import com.loacg.konachan.utils.HttpClient;
import com.loacg.konachan.utils.JsonUtils;
import com.loacg.konachan.utils.multidown.MultithreadingDownload;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Project: konachan
 * Author: Sendya <18x@loacg.com>
 * Time: 2016/7/12 09:17 PM
 */
public class Test {

    public static void main(String[] args) {
        int threadNum = 5;
        long block = 0L;

        String reJson = HttpClient.get("https://yande.re/post.json?tags=touhou&page=2");
        if (StringUtils.isEmpty(reJson)) {
            throw new RuntimeException("konachan is not return data");
        }

        List<?> list = JsonUtils.json2List(reJson);

        System.out.println("总数量：" + list.size());

        for (Object obj : list) {
            Map<?, ?> map = (Map<?, ?>) obj;

            String url = map.get("jpeg_url").toString();
            System.out.println("file_url: " + map.get("file_url"));
            System.out.println("jpeg_url: " + url);
            System.out.println("width: " + map.get("width"));
            System.out.println("height: " + map.get("height"));
            String fileName = "D:\\sdk\\tmp\\new\\" + url.substring(url.lastIndexOf("/yande.re%20") + "/yande.re%20".length());

            try {
                URL u = new URL(map.get("jpeg_url").toString());
                HttpURLConnection conn = (HttpURLConnection)u.openConnection();
                long len = conn.getContentLength();
                conn.disconnect();
                Path path = Paths.get(fileName);

                new MultithreadingDownload().download(path.toFile(), len, threadNum, u);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
