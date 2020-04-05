package com.jt;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

//测试hash/list/事务控制
public class testHash {
	@Test
	public void testHash1() {
		Jedis jedis = new Jedis("192.168.114.131",6379);
		jedis.hset("user1", "id", "200");
		jedis.hset("user1", "name","tomcat服务器");
		String result = jedis.hget("user1", "name");
		System.out.println(result);
	}
	
	//编辑list集合
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.114.131",6379);
		jedis.lpush("list", "1","2","3","4","5");
		System.out.println(jedis.rpop("list"));
	}
	//redis事务控制
	@Test
	public void testTX() {
		Jedis jedis = new Jedis("192.168.114.131",6379);
		Transaction multi = jedis.multi();
		try {
			multi.set("ww", "ww");
			multi.set("dd",null);
			multi.exec();
		}catch(Exception e) {
			multi.discard();
		}
	}
}
