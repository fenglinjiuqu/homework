

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaChong {
    private static final String URL="https://tieba.baidu.com/p/2256306796?red_tag=1781367364";
    private static final String ECODING="UTF-8";
    private static final String IMGURL_REG="<img.*src=(.*?)[^>]*?>";
    private  static final String IMGSRC_REG="http:.+(\\.jpeg|\\.jpg|.png)\"";
    public static void main(String[] args) {

        try {
            PaChong pa = new PaChong();
            String HTML = pa.getHtml(URL);
            List<String>imgUrl=pa.getImageUrl(HTML);
            List<String>imgSrc=pa.getImageSrc(imgUrl);
            pa.Dowmload(imgSrc);
        } catch (Exception e) {
            System.out.println("发生错误");
        }

    }
    //获取html内容
    String getHtml(String url) throws Exception {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        InputStream in = connection.getInputStream();
        InputStreamReader input = new InputStreamReader(in);
        BufferedReader buf = new BufferedReader(input);
        String line;
        StringBuffer sbuf = new StringBuffer();
        while((line=buf.readLine())!=null){
            sbuf.append(line,0,line.length());
            sbuf.append('\n');
        }
        input.close();
        buf.close();
        in.close();
        return  sbuf.toString();
    }
    //获取imageUrl地址
    List<String> getImageUrl(String html) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> listimgurl = new ArrayList<String>();
        while(matcher.find()){
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }
    //获取imageSrc地址
    List<String> getImageSrc(List<String> listimageUrl) {
        List<String> listimgsrc = new ArrayList<String>();
        for(String image:listimageUrl){
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while(matcher.find()){
                listimgsrc.add(matcher.group().substring(0,matcher.group().length()-1));
            }
        }
        return listimgsrc;
    }
    //下载图片
    void Dowmload(List<String> listimgSrc){
      try {
          Date begindate = new Date();
          for (String url : listimgSrc) {
              Date begindate2 = new Date();
              String imagename = url.substring(url.lastIndexOf("/") + 1, url.length());
              URL url2 = new URL(url);
              InputStream in = url2.openStream();
              FileOutputStream out = new FileOutputStream(new File("src/res/" + imagename));
              byte[] bytes = new byte[1024*8];
              int length = 0;
              System.out.println("开始下载：" + url);
              while ((length = in.read(bytes, 0, bytes.length)) != -1) {
                  out.write(bytes, 0, length);
              }
              in.close();
              out.close();
              System.out.println(imagename+"下载完成");
              Date overdate2 = new Date();
              double time = overdate2.getTime() - begindate2.getTime();
              System.out.println("耗时" + time / 1000 + "s");
          }
          Date overdate = new Date();
          double time = overdate.getTime() - begindate.getTime();
          System.out.println("总耗时" + time / 1000 + "s");
      }catch (Exception e){
          System.out.println("下载失败");
      }
    }
}
