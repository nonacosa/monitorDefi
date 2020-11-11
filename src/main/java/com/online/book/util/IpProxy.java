package com.online.book.util;



import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 这个DEMO主要为了测试动态代理IP的稳定性
 * 也可以作为爬虫参考项目，如需使用，请自行修改代码webParseHtml方法
 */
public class IpProxy {
	public static List ipList = new ArrayList<>();
	public static String ip_address;
	public static boolean gameOver = false;
	public static void main(String[] args) {
//		getRandomIp();
		TimerGetIp();

	}

	public static void TimerGetIp() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
//				System.out.println("-------每隔 1S 获取一次时间 -------- :" + ip_address);
				getRandomIp();
			}
		}, 0, 300);
	}

	public static Proxy randomProxy(){
		var ip_port = ip_address.split(":");
		var ip = ip_port[0].split(",")[0];
		var port = Integer.parseInt(ip_port[1]);
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,port));
	}

	public static String getRandomIp(){
		String ipp = null;
		String order = "a36d2962961463df882108e8dd62a167";
		try {
			java.net.URL url = new java.net.URL("http://dynamic.goubanjia.com/dynamic/get/" + order + ".html?ttl");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(3000);
			connection = (HttpURLConnection)url.openConnection();

			InputStream raw = connection.getInputStream();
			InputStream in = new BufferedInputStream(raw);
			byte[] data = new byte[in.available()];
			int bytesRead = 0;
			int offset = 0;
			while(offset < data.length) {
				bytesRead = in.read(data, offset, data.length - offset);
				if(bytesRead == -1) {
					break;
				}
				offset += bytesRead;
			}
			in.close();
			raw.close();
			String[] res = new String(data, "UTF-8").split("\n");
			List ipList = new ArrayList<>();
			for (String ip : res) {
				try {
					String[] parts = ip.split(",");
					if (Integer.parseInt(parts[1]) > 0) {
						ipList.add(parts[0]);
					}
				} catch (Exception e) {
				}
			}
			if (ipList.size() > 0) {
				ipp = String.valueOf(ipList.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(">>>>>>>>>>>>>>获取IP出错");
		}
		if(ipp != null) {
			ip_address = ipp;
		}
//		System.out.println(ipp);
		return ipp;
	}


}
