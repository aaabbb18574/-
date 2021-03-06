package org.air.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

/**
 * arbt爬虫入口  http://www.ps998.pw/
 * @date 2019年1月26日19:54:31
 * @author Air
 * @since 1.0
 */
public class AiRenBtSpider {

    public static void main(String[] args) {
        long s =  System.currentTimeMillis();

        //爬到多少页
        int page = 9;
        //二级页面地址
        String baseUrl = "http://www.ps998.pw/a/avwm/index";
        String url;

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 50, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));

        while( page > 0 ){
           if(page == 1){
               url = baseUrl + ".html";
           }else{
               url = baseUrl + '_' + page + ".html";
           }
            System.out.println("开始爬 " +  url );
            threadPool.execute(new SpiderRunable(url));

            --page;
        }
        while( true ){
            if(threadPool.getActiveCount() < 1){
                long e = System.currentTimeMillis();
                System.out.println("全部爬取任务完成! 总耗时s： " + ( (e - s)/1000 ) ) ;
                threadPool.shutdown();
                break;
            }
        }
    }
}

class SpiderRunable implements  Runnable{
    private String url;

    public SpiderRunable(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        if(null == url || url.trim().isEmpty()){
            return;
        }
        doSpider();
    }

    private void doSpider(  ) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements all = doc.select( "li > a" ) ;
            String href;
            Elements btEl;
            File dir = new File("F:\\Downloads\\bt\\需要下载");

            for (Element element : all) {
                href = "http://www.ps998.pw/" + element.attr("href");
                System.out.println("发现了 BT页面地址： " + href);
                doc = Jsoup.connect(href).get();
                btEl = doc.select( "div .pagecon" ) ;
                href = btEl.text().substring(11);
                System.out.println("获取到 BT页面的 bt种子地址 信息开始下载： " + href);
                downLoadFromUrl(href, new File( dir, doc.title() + ".torrent"));
            }
            System.out.println("下载完成： " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从网络Url中下载文件
     * @param urlStr http file url
     * @param saveFile download to localfile
     */
    private static void  downLoadFromUrl(String urlStr,File saveFile) throws IOException{
        if(saveFile.exists()){
            System.out.println("文件已经存在！ 跳过！");
            return;
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        FileOutputStream fos = new FileOutputStream(saveFile);
        fos.write(getData);

        fos.close();
        inputStream.close();

        System.out.println("info:"+url+" download success");
    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
