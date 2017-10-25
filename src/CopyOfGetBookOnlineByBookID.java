import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 *	批处理获取文件名命令：
 *	DIR *.* /B/OD >LIST.TXT
 */
public class CopyOfGetBookOnlineByBookID {

	final static String path = "C:/Users/Administrator/Desktop/";
	final static String ID = "3439596";
	final static String url = "http://book.qidian.com/info/";

	public static void main(String[] args) {
		FileOutputStream fs = null;
		try {
			Document doc = Jsoup.connect(url+ID).get();
			Elements startUrlTemp = doc.getElementsByClass("J-getJumpUrl");
			String name = doc.title();
			String startUrl = null;
			if (startUrlTemp != null)
				startUrl = "https:" + startUrlTemp.attr("href");
			fs = new FileOutputStream(new File(path + name + ".txt"));
			PrintStream p = new PrintStream(fs);
			if(startUrl != null)
				Write(startUrl, p);
			p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Finish");
	}

	public static int Write(String url, PrintStream write) {
		String urlNext = null;
		try {
			Document doc = Jsoup.connect(url).get();
			String title = doc.getElementsByClass("j_chapterName").text();
			Elements temp = doc
					.getElementsByClass("read-content j_readContent");
			String content = temp.select("p").val("\n").html();
			Element urlNextTips = doc.getElementById("j_chapterNext");
			if (urlNextTips!= null)
				urlNext = "https:" + urlNextTips.attr("href");

			write.println(title);
			write.println(content);

			System.out.println(title);
		} catch (IOException e) {
			System.out.println("加载网页失败：\t" + url);
			e.printStackTrace();
		}

		if (urlNext!=null)
			return Write(urlNext, write);
		else{
			return 0;
		}
	}
}
