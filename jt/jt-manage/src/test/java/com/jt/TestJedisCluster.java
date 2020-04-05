package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {
	@Test
	public void test() {
		Set<HostAndPort> sets = new HashSet<>();
		sets.add(new HostAndPort("192.168.114.131",7000));
		sets.add(new HostAndPort("192.168.114.131",7001));
		sets.add(new HostAndPort("192.168.114.131",7002));
		sets.add(new HostAndPort("192.168.114.131",7003));
		sets.add(new HostAndPort("192.168.114.131",7004));
		sets.add(new HostAndPort("192.168.114.131",7005));
		JedisCluster cluster = new JedisCluster(sets);
		cluster.set("aa", "test");
		System.out.println(cluster.get("aa"));
	}
}
