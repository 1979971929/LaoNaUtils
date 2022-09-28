package xyz.pyming.utils.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: http工具类
 * @author: LaoNa
 * @Date 2022-9-28
 */
public class HttpUtils {
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000000).
                setConnectionRequestTimeout(1000000).setSocketTimeout(1000000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }


    @Test
    public void httpUtilsTest() {
        try {

            long a2 = new Date().getTime();
            System.out.println(downloadEnCryptionFile("http://www.hrbpf.gov.cn/attachment.do?id=39d6da11c6314c5e88da5f852a1cc76b", "H:\\filetest\\2.xls"));
            long b2 = new Date().getTime();
            System.out.println("==" + (b2 - a2));
            long a1 = new Date().getTime();
            System.out.println(downloadFile("http://www.hrbpf.gov.cn/attachment.do?id=39d6da11c6314c5e88da5f852a1cc76b", "H:\\filetest\\1.xls"));
            long b1 = new Date().getTime();
            System.out.println("==" + (b1 - a1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String sendPost(String url) throws Exception {
        return sendPost(url, null, CHARSET);
    }

    public static String sendPost(String url, Map<String, String> params) throws Exception {
        return sendPost(url, params, CHARSET);
    }

    public static String sendGet(String url) throws Exception {
        return sendGet(url, null, CHARSET);
    }

    public static String sendGet(String url, Map<String, String> params) throws Exception {
        return sendGet(url, params, CHARSET);
    }

    /**
     * 发送post
     *
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, Map<String, String> params, String charset) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }
        //拼接参数列表
        List<NameValuePair> nvps = null;
        if (params != null && !params.isEmpty()) {
            nvps = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }

        }
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        if (nvps != null && nvps.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));

        }
        //模拟表单提交
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        //模拟浏览器
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort(); //释放请求
            throw new RuntimeException("请求状态码为：" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }

    /**
     * 发送postjson
     *
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static String sendPostJson(String url, String json) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        if (StringUtils.isNotBlank(json)) {
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            //设置参数到请求对象中
            httpPost.setEntity(requestEntity);
        }
        //模拟表单提交
        httpPost.setHeader("Content-type", "application/json");
        //模拟浏览器
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort(); //释放请求
            throw new RuntimeException("请求状态码为：" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity, CHARSET);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }


    /**
     * 发送get
     *
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    public static String sendGet(String url, Map<String, String> params, String charset) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }

        //拼接参数列表
        List<NameValuePair> nvps = null;
        if (params != null && !params.isEmpty()) {
            nvps = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(nvps, charset));
        }
        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);

        //模拟表单提交
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
        //模拟浏览器
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort(); //释放请求
            throw new RuntimeException("请求状态码为：" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity, charset);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }


    /**
     * 发送get
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String sendGetByUrl(String url) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }

        //拼接参数列表
        List<NameValuePair> nvps = null;

        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);

        //模拟表单提交
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
        //模拟浏览器
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpGet.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 302) {
            httpGet.abort(); //释放请求
            result = (response.getFirstHeader("Location").getValue());
        }

        //释放链接
        response.close();
        return result;
    }


    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String downloadFile(String url, String filePath) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }

        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);

        //模拟表单提交
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
        //模拟浏览器
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort(); //释放请求
            throw new RuntimeException("请求状态码为：" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            File file = new File(filePath);
            if (!file.getParentFile().exists() && !file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fs = new FileOutputStream(file);
            byte[] bytes = new byte[1204 * 8];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                fs.write(bytes, 0, len);
            }
            is.close();
            fs.close();


            Header headers[] = response.getAllHeaders();
            int ii = 0;
            while (ii < headers.length) {
                // System.out.println(headers[ii].getName() + ":" + headers[ii].getValue());
                ++ii;
            }
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String downloadEnCryptionFile(String url, String filePath) throws Exception {
        String result = "";
        if (StringUtils.isBlank(url)) {
            return null;
        }
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);

        //模拟表单提交
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
        //模拟浏览器
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort(); //释放请求
            throw new RuntimeException("请求状态码为：" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            File file = new File(filePath);

            if (!file.getParentFile().exists() && !file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new
                    FileOutputStream(filePath));
            int n;
            while ((n = bis.read()) != -1) {
                bos.write(n + 123);
            }
            bis.close();
            bos.close();
            Header headers[] = response.getAllHeaders();
            int ii = 0;
            while (ii < headers.length) {
                System.out.println(headers[ii].getName() + ":" + headers[ii].getValue());
                ++ii;
            }
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }
}
