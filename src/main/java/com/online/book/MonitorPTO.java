package com.online.book;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.online.book.util.Money;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;

import io.prometheus.client.exporter.PushGateway;
import io.prometheus.client.Gauge;
import lombok.SneakyThrows;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenda.zhuang
 * @Date 2020/11/9 7:23 下午
 * @Description pto 交易对
 * @E-mail sis.nonacosa@gmail.com
 */
public class MonitorPTO {

	/**
	 * push网关
	 *
	 * @return
	 */
	@Bean
	public PushGateway getPushGateway() {
		return new PushGateway("localhost:9091");
	}




	@SneakyThrows
	public static void main(String[] args)   {
		try {
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MonitorPTO.class);
			PushGateway gateway = context.getBean(PushGateway.class);
			Gauge pto = Gauge.build().name("pto_eos").help("pto_eos_detail").register();
			Gauge pto_eos_eos_count = Gauge.build().name("pto_eos_eos_count").help("pto_eos_eos_count_detail").register();
			Gauge pto_eos_pto_count = Gauge.build().name("pto_eos_pto_count").help("pto_eos_pto_count_detail").register();


			Gauge daf = Gauge.build().name("daf_eoss").help("daf_eoss_detail").create();
			Gauge daf_eos_eos_count = Gauge.build().name("daf_eoss_eos_count").help("daf_eos_eos_count_detail").register();
			Gauge daf_eos_daf_count = Gauge.build().name("daf_eoss_daf_count").help("daf_eos_daf_count_detail").register();

			Gauge candy = Gauge.build().name("candy_eos").help("candy_eos_detail").create();
			while (true) {
				pto(pto,pto_eos_eos_count,pto_eos_pto_count,gateway);
				daf(daf,daf_eos_eos_count,daf_eos_daf_count,gateway);
				candy(candy,gateway);
				Thread.sleep(5000);
			}
		}catch (Exception e) {
			System.out.println(e);
			send("小事儿，莫慌");
		}
	}

	/**
	 * pto - eos
	 * @param gateway
	 */
	@SneakyThrows
	public static void pto(Gauge gauge,Gauge eosCount,Gauge otherCount,PushGateway gateway){

		Double lastPrise = gauge.labels().get();
		List<Map> data = fetch();
		Double ptoPrise = Double.parseDouble((String) data.get(0).get("price1_last"));
		Double eosCountt = Double.parseDouble(((String) data.get(0).get("reserve0")).split(" ")[0]);
		Double otherCountt = Double.parseDouble(((String) data.get(0).get("reserve1")).split(" ")[0]);
		if(ptoPrise > lastPrise && Money.changeDegreeRange(lastPrise,ptoPrise,0.001)) {
			send("PTO ↗️ ：" + ptoPrise + "个 EOS ( 100 : " + 100 / ptoPrise + ")" );
		}
		if(ptoPrise < lastPrise && Money.changeDegreeRange(lastPrise,ptoPrise,0.001)) {
			send("PTO ↘️ ：" + ptoPrise + "个 EOS ( 100 : " + 100 / ptoPrise + ")" );
		}
		gauge.set(ptoPrise);
		eosCount.set(eosCountt);
		otherCount.set(otherCountt);
		gateway.push(gauge, "pto_eos");
		gateway.push(eosCount, "pto_eos_eos_count");
		gateway.push(otherCount, "pto_eos_pto_count");
	}


	/**
	 * daf - eos
	 * @param gateway
	 */
	@SneakyThrows
	public static void daf(Gauge gauge,Gauge eosCount,Gauge otherCount,PushGateway gateway){

		Double lastPrise = gauge.labels().get();
		List<Map> data = fetch();
		Double ptoPrise = Double.parseDouble((String) data.get(29).get("price1_last"));
		Double eosCountt = Double.parseDouble(((String) data.get(29).get("reserve0")).split(" ")[0]);
		Double otherCountt = Double.parseDouble(((String) data.get(29).get("reserve1")).split(" ")[0]);
		if(ptoPrise > lastPrise && Money.changeDegreeRange(lastPrise,ptoPrise,0.001)) {
			send("DAF ↗️  ：" + ptoPrise + "个 EOS ( 100 : " + 100 / ptoPrise + ")" );
		}
		if(ptoPrise < lastPrise && Money.changeDegreeRange(lastPrise,ptoPrise,0.001)) {
			send("DAF ↘️  ：" + ptoPrise + "个 EOS ( 100 : " + 100 / ptoPrise + ")" );
		}
		gauge.set(ptoPrise);
		eosCount.set(eosCountt);
		otherCount.set(otherCountt);
		gateway.push(eosCount, "daf_eoss_eos_count");
		gateway.push(otherCount, "daf_eoss_daf_count");
		gateway.push(gauge, "daf_eoss");
	}


	/**
	 * candy - eos
	 * @param gateway
	 */
	@SneakyThrows
	public static void candy(Gauge gauge,PushGateway gateway){

		Double lastPrise = gauge.labels().get();
		List<Map> data = fetch();
		Double eos_count = Double.parseDouble(String.valueOf(data.get(8).get("reserve0")).split(" ")[0]);
		Double candy_count = Double.parseDouble(String.valueOf(data.get(8).get("reserve1")).split(" ")[0]);
		Double prise = candy_count / eos_count;
		if(prise > lastPrise && Money.changeDegreeRange(lastPrise,prise,0.001)) {
			send("CANDY ↗️ ：" + prise + "个 EOS");
		}
		if(prise < lastPrise && Money.changeDegreeRange(lastPrise,prise,0.001)) {
			send("CANDY ↘️ ：" + prise + "个 EOS");
		}
		gauge.set(prise);
		gateway.push(gauge, "daf_eos");
	}



	@SneakyThrows
	public static void  send(String  msg) {
		HttpClient httpclient = HttpClients.createDefault();
		String d = " {\n" + "        \"msgtype\": \"text\",\n" + "        \"text\": {\n" + "            \"content\": " +
				"\""+msg+"\"\n" + "        }\n" + "   }";
		HttpPost httppost = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=dab2cb68-fbe6-4432-b98e-d71d933ef669");
		httppost.addHeader("Content-Type", "application/json; charset=utf-8");
		StringEntity se = new StringEntity(d, "utf-8");
		httppost.setEntity(se);

		httpclient.execute(httppost);
	}



	/**
	 * 发送post请求
	 * @return
	 * @throws IOException
	 */
	public static List<Map> fetch( ) throws Exception, IOException {
		Map map = new HashMap<>();
		map.put("code", "ptoswapaccts");
		map.put("scope", "ptoswapaccts");
		map.put("table", "markets");
		map.put("json", "true");
		map.put("limit", "100");

		String jsonString = JSON.toJSONString(map);
		JSONObject json = JSON.parseObject(jsonString);
		String body = "";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://eos.blockeden.cn/v1/chain/get_table_rows");
		StringEntity s = new StringEntity(json.toString(), "utf-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(s);
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		CloseableHttpResponse response = client.execute(httpPost);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			body = EntityUtils.toString(entity, "utf-8");
		}
		EntityUtils.consume(entity);
		response.close();
		return JSON.parseObject(JSON.parseObject(body).getString("rows"),List.class);
	}

}
