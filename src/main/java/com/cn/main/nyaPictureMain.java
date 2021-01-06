package com.cn.main;

import com.cn.util.HttpClientUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class nyaPictureMain {

    //存放目录
    private static String fileSource = "E://nyaManhua//new//";

    public static void main(String[] args) throws Exception {


        List<String> urlList = new ArrayList<String>();

        //地址
        urlList.add("https://zha.doghentai.com/g/338012/");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");
        urlList.add("");





        nyaPictureMain.crawlerNyaUrl(urlList);
        String exSite = "cmd /c start " + fileSource ;
        Runtime.getRuntime().exec(exSite);

    }


    public static void crawlerNyaPic(int picSum,String fileUrl,String intputFile,String suffix){

        try {
            for (int i = 1; i <= picSum; i++) {
//                suffix = ".jpg"; //随时替换文件格式
                CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建HttpClient实例
                HttpGet httpGet = new HttpGet(fileUrl+i+suffix); // 创建Httpget实例
                //设置Http报文头信息
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
                httpGet.setHeader("accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
                httpGet.setHeader("accept-encoding", "gzip, deflate, br");
                httpGet.setHeader("referer", "https://zha.doghentai.com/");
                httpGet.setHeader("sec-fetch-dest", "image");
                httpGet.setHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");

                HttpHost proxy = new HttpHost("127.0.0.1", 7890);
                //超时时间单位为毫秒
                 RequestConfig defaultRequestConfig = RequestConfig.custom()
                         .setConnectTimeout(1000).setSocketTimeout(30000)
                         .setProxy(proxy).build();
                httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();


                CloseableHttpResponse response = null;
                response = httpClient.execute(httpGet); // 执行http get请求
                HttpEntity entity = response.getEntity(); // 获取返回实体
                if(null != entity){
                    InputStream inputStream = entity.getContent();//返回一个输入流
                    //输出图片
                    FileUtils.copyInputStreamToFile(inputStream, new File(intputFile+i+suffix));//引用org.apache.commons.io.FileUtils
                    System.out.println(i+suffix);
                }
                response.close(); // 关闭response
                httpClient.close(); // 关闭HttpClient实体

            }

        }catch (Exception e){
            System.out.println(e);
        }
    }


    public static void crawlerNyaUrl(List<String> urlList) throws Exception {

        Integer rateDow = 1;
        for(String url:urlList){
            String html = "";
            if(url.length() != 0){
                html = HttpClientUtil.getSource(url);

                Document document = Jsoup.parse(html);
                Element element = document.selectFirst("div.container").selectFirst("a");
                String coverImgUrl = element.select("img").attr("data-src");

                //获取图片载点
                String[] ourStr = coverImgUrl.split("/");
                //获取后缀
                String[] oursuffix = coverImgUrl.split("\\.");
                //获取数量
                Elements picSum = document.select("div.thumb-container");
                //获取本子名字
                String benziName = element.select("img").attr("alt");
                benziName = benziName.replaceAll("\\?","").replaceAll(":","").replaceAll(" ","").replaceAll("\\*","");

                int count = picSum.size();
                int benziN = Integer.parseInt(ourStr[ourStr.length-2]);
                String suffix = "."+oursuffix[oursuffix.length-1];
                String fileUrl = "https://i0.nyacdn.com/galleries/"+benziN+"/";
                String intputFile = fileSource +benziName +"//";
                nyaPictureMain.crawlerNyaPic(count,fileUrl,intputFile,suffix);

                //缓存完后暂停几秒
                Thread.sleep(3000);
            }
        }

        System.out.println("喵变态图片缓存成功！！！！");



    }


}
