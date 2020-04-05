package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	//测试哨兵get/set操作
	@Test
	public void test01() {
		//masterName 代表主机的变量名称
		//sentinels Set<String> IP：端口
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.114.131:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster",sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("a", "a");
		System.out.println(jedis.get("a"));
		jedis.close();
	}
}
