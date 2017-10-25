import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 *	批处理获取文件名命令：
 *	DIR *.* /B/OD >LIST.TXT
 */
public class GetBook {
	static String path = "C:/Users/Administrator/Desktop/get/";
	final static String command = "DIR *.* /B/OD >LIST.TXT";
	
	public static void main(String[] args) {
		path = System.getProperty("user.dir") + "/";
		try {
			Process proc = Runtime.getRuntime().exec("cmd /c" + "cd " + path + "&&" + command);
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(new File(path + "book.txt"));
			PrintStream p = new PrintStream(fs);
			FileInputStream in;
			in = new FileInputStream(path + "LIST.TXT");
			InputStreamReader inReader = new InputStreamReader(in);  
	        BufferedReader bufReader = new BufferedReader(inReader);  
	        String name = null;  
	        
	        while((name = bufReader.readLine()) != null){  
	        	Write(name,p);
	        }  
	        
	        bufReader.close();  
	        inReader.close();  
	        in.close();  
	        p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
		
		
		System.out.println("Finish");
	}

	public static void Write(String name,PrintStream  write){
		File input = new File(path + name); 
		Document doc = null;
		
		try {
			doc = Jsoup.parse(input,"UTF-8","");
		} catch (IOException e) {
			write.println("读取文件失败: " + name);
			e.printStackTrace();
		}
		
		String title = doc.getElementsByClass("j_chapterName").text(); 
		Elements content = doc.getElementsByClass("read-content j_readContent"); 
		String temp =content.select("p").val("\n").html();
		
		write.println(title);
		write.println(temp);
		
		System.out.println(name);
	}
}
