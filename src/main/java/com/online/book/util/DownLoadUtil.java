package com.online.book.util;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.*;

public class DownLoadUtil implements X509TrustManager {

	public static void main(String[] args) {
		/*从https下载文件,并保存到桌面,文件名字段获取*/
		String path = "/Users/Venda-GM/Downloads/";
//		String urls="https://t1.huanqiu.cn/8093461e439c770c514176d9d1f4573a.jpg;https://t1.huanqiu.cn/8093461e439c770c514176d9d1f4573a.jpg";
		String urls="https://www.liebianwangpan.com/s/kCSpHR8zspCY2e5/download?xxx.xxx";
		if(!(urls==null|| StringUtils.equals("", urls))){
			String[] arr0=urls.split(";");
			for(int h=0;h<arr0.length;h++){
				String url=arr0[h];
				if(!(url==null||StringUtils.equals("",url))){
					String[] arr1=url.split("/");
					if(arr1.length>0){
						String dictory=path;
						String fileName=arr1[arr1.length-1];
						fileName=(fileName!=null&&fileName.indexOf("?")==-1)?fileName:(fileName.substring(0, fileName.indexOf("?")));
						System.out.println(fileName);
						try {
							downLoadFromUrlHttps(url,fileName, dictory,false);
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
				}
			}
		}
		/**
		 * 从http获取文件,文件名自己命名
		 */
		try {
			downLoadFromUrlHttp("http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg", "NBA.jpg", path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 处理https GET/POST请求 请求地址、请求方法、参数
	 */
	public static String httpsRequest(String requestUrl, String requestMethod,
									  String outputStr) {
		StringBuffer buffer = null;
		try {
			// 创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManager[] tm = { new DownLoadUtil() };
			// 初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			;
			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			// url对象
			URL url = new URL(requestUrl);
			// 打开连接
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			/**
			 * 这一步的原因: 当访问HTTPS的网址。您可能已经安装了服务器证书到您的JRE的keystore
			 * 但是服务器的名称与证书实际域名不相等。这通常发生在你使用的是非标准网上签发的证书。
			 *
			 * 解决方法：让JRE相信所有的证书和对系统的域名和证书域名。
			 *
			 * 如果少了这一步会报错:java.io.IOException: HTTPS hostname wrong: should be localhost
			 */
			conn.setHostnameVerifier(new DownLoadUtil().new TrustAnyHostnameVerifier());
			// 设置一些参数
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestMethod);
			// 设置当前实例使用的SSLSoctetFactory
			conn.setSSLSocketFactory(ssf);
			conn.connect();
			// 往服务器端的参数
			if (null != outputStr) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("utf-8"));
				os.close();
			}
			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			//读取内容
			InputStreamReader isr = new InputStreamReader(is,"utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private static AtomicInteger count = new AtomicInteger(0);
	public static void downLoadFromUrlHttps(String urlStr, String fileName,
											String savePath,Boolean proxy) throws Exception {
		Thread.sleep(200L);
		// 创建SSLContext
		SSLContext sslContext = SSLContext.getInstance("SSL");
		TrustManager[] tm = { new DownLoadUtil() };
		// 初始化
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 获取SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// url对象
		URL url = new URL(urlStr);
//		count.addAndGet(1);
//		InetSocketAddress addr = new InetSocketAddress("115.210.68.250",50040);
		Proxy proxySetting = null;
		if(proxy) {
			proxySetting = IpProxy.randomProxy();
		}
//		var ipPort = IpProxy.getRandomIp();
//		if (ipPort != null) {
//			System.out.println("获取IP成功：" + ipPort);
//			var ip_port = ipPort.split(":");
//			var ip = ip_port[0].split(",")[0];
//			var port = Integer.parseInt(ip_port[1]);
//			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,port));
//		}
		// http 代理
//		if(count.get() > 20) {
//			count.set(0);

//		}
		HttpsURLConnection conn;
		// 打开连接
		if(proxy) {
			conn = (HttpsURLConnection) url.openConnection(proxySetting);
		}else {
			conn = (HttpsURLConnection) url.openConnection();
		}

		/**
		 * 这一步的原因: 当访问HTTPS的网址。您可能已经安装了服务器证书到您的JRE的keystore
		 * 但是服务器的名称与证书实际域名不相等。这通常发生在你使用的是非标准网上签发的证书。
		 *
		 * 解决方法：让JRE相信所有的证书和对系统的域名和证书域名。
		 *
		 * 如果少了这一步会报错:java.io.IOException: HTTPS hostname wrong: should be <localhost>
		 */
		conn.setHostnameVerifier(new DownLoadUtil().new TrustAnyHostnameVerifier());
		// 设置一些参数
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		// 设置当前实例使用的SSLSoctetFactory
		conn.setSSLSocketFactory(ssf);
		conn.connect();


		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		//输出流
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}


	/**
	 * 从网络http类型Url中下载文件
	 *
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrlHttp(String urlStr, String fileName,
										   String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		System.out.println("connect before");
		conn.connect();

		// 得到输入流
		System.out.println("getInputStream before");
		InputStream inputStream = conn.getInputStream();
		System.out.println("readInputStream before");
		byte[] getData = readInputStream(inputStream);
		System.out.println("readInputStream after");
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		// 输出流
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}


	/**
	 * 从输入流中获取字节数组
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] b = new byte[10240];
		int len = 0;
		int downloadLength = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int count = 0;
		while ((len = inputStream.read(b)) != -1) {
			count ++;
			downloadLength += len;
			System.out.println(count + "读写中...");
			bos.write(b, 0, len);
//			if(inputStream.readAllBytes().length == downloadLength){		//防止最后一次读取的时候，一直阻塞
//				System.out.println("哈哈哈");
//				break;
//			}

		}


		bos.close();
		return bos.toByteArray();
	}


	/***
	 * 校验https网址是否安全
	 *
	 * @author solexit06
	 *
	 */
	public class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			// 直接返回true:默认所有https请求都是安全的
			return true;
		}
	}


	/*
	 * 里面的方法都是空的，当方法为空是默认为所有的链接都为安全，也就是所有的链接都能够访问到 当然这样有一定的安全风险，可以根据实际需要写入内容
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}


	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}


	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
