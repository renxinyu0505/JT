package com.jt;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	/**
	 * 测试HttpClient
	 * 1.实例化httpClient对象
	 * 2.定义http请求路径url uri
	 * 3.定义请求方式 get post
	 * 4.利用API发起http请求
	 * 5.获取返回值以后判断状态信息 200
	 * 6.获取响应数据
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Autowired
	private CloseableHttpClient client;
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		//1.实例化httpClient对象
//		CloseableHttpClient client = HttpClients.createDefault();
		//2.定义http请求路径url uri
		String url = "http://www.baidu.com";
		//3.定义请求方式 get post
		HttpGet httpGet = new HttpGet(url);
		//4.利用API发起http请求
		CloseableHttpResponse response = client.execute(httpGet);
		//5.获取返回值以后判断状态信息 200
		 
		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("请求成功");
			//6.获取响应数据
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println(result);
		}else {
			throw new RuntimeException();
		}
	}
	@Autowired
	private HttpClientService service;
	//测试工具utils
	@Test
	public void testUtils() {
		String url = "https://www.baidu.com/s?ie=UTF-8&wd=%E7%99%BE%E5%BA%A6";
		String result = service.doGet(url);
		System.out.println(result);
	}
}
