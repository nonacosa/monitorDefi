package com.online.book.util;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.CheckedOutputStream;

/**
 * @author wenda.zhuang
 * @Date 2020/8/20 6:54 下午
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class ImageUtil {

	public static void saveImageFromUrl(String fileUrl,String fileName) throws IOException {
		FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
	}

	public static boolean saveImageFromUrl2(String fileUrl,String fileName) throws IOException {
		boolean success = false;
		var kb = 0;
		try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
			 FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
				kb ++;
			}
			System.out.println(dataBuffer.length / 1000);
			if(kb > 2) {
				success =  true;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return success;
	}


		public static void saveImageFromUrl3(String fileUrl,String fileName) throws IOException {
			URL url1 = new URL(fileUrl);
			URLConnection uc = url1.openConnection();
			InputStream inputStream = uc.getInputStream();

			FileOutputStream out = new FileOutputStream(fileName);
			int j = 0;
			while ((j = inputStream.read()) != -1) {
				out.write(j);
			}
			inputStream.close();


		}


	public static void main(String[] args) throws IOException {
		saveImageFromUrl2("https://7sebook.s3.us-west-002.backblazeb2.com/2020/08/03/批判性思维第11版中文版nyjsfdfht七秒电子书7sebook.azw3","xxx.azw3");
	}

}
