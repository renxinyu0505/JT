package com.jt;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.User;

public class TestRedis {
	
	/**
	 * 1.首先获取对象的getXXX方法
	 * 2.把get去掉，之后首字母小写获取属性的名称
	 * 3.之后将属性的名称：属性的值进行拼接。
	 * 4.形成JSON串
	 * @throws JsonProcessingException
	 */
	@Test
	public void userToJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		User user = new User();
		user.setId(11);
		user.setName("张三");

		String JSON = mapper.writeValueAsString(user);

		System.out.println(JSON);
	}
	/**
	 *	1. 获取userJSON串
	 *	2.根据class类型反射机制，实例化对象
	 *	3.根据对象的set方法 获取属性的值
	 *	4.最终生成对象
	 * @throws JsonProcessingException
	 */
	@Test
	public void JSONToUser() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		User user = new User();
		user.setId(11);
		user.setName("张三");

		String JSON = mapper.writeValueAsString(user);
		//以下方法实现了数据的转化
		User res = mapper.readValue(JSON,User.class);
		System.out.println(res);
	}
}

