package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 实现spring 容器管理Redis配置类
 * @author Administrator
 *
 */
@Configuration//xml
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	//	@Value("${jedis.host}")
	//	private String host;
	//	@Value("${jedis.port}")
	//	private Integer port;
	//	
	//	@Bean
	//	@Scope("prototype")
	//	public Jedis jedis() {
	//		return new Jedis(host,port);
	//	}
	/**
	 * redis分片
	 */
//	@Value("${redis.nodes}")
//	private String nodes;
//	/**
//	 * redis分片
//	 */
//	@Bean
//	@Scope("prototype")
//	public ShardedJedis shardedJedis() {
//		List<JedisShardInfo> shards = new ArrayList<>();
//		String[] redisNodes = nodes.split(",");
//		for(String redisNode : redisNodes) {
//			//redisNode : IP : port
//			String[] node = redisNode.split(":");
//			String host = node[0];
//			int port = Integer.parseInt(node[1]);
//			JedisShardInfo info = new JedisShardInfo(host,port);
//			shards.add(info);
//		}
//		return new ShardedJedis(shards);
//	}
	/*
	 * redis哨兵
	 */
//	@Value("${redis.sentinels}")
//	private String jedisSentinelNodes;
//	@Value("${redis.sentinels.masterName}")
//	private String masterName;
//	@Bean
//	public JedisSentinelPool jedisSentinelPool() {
//		
//		Set<String> sentinels = new HashSet<>();
//		
//		sentinels.add(jedisSentinelNodes);
//		
//		return new JedisSentinelPool(masterName, sentinels);
//	}
	/*
	 * redis集群
	 */
	@Value("${redis.nodes}")
	private String redisNodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		//根据，号拆分多个ip
		Set<HostAndPort> nodes = new HashSet<>();
		String[] IP = redisNodes.split(",");
		for(String hostAndPort : IP) {
			
			String host = hostAndPort.split(":")[0];
			int port =Integer.parseInt(hostAndPort.split(":")[1]);
			nodes.add(new HostAndPort(host,port));
		}
		return new JedisCluster(nodes);
		
	}
}
