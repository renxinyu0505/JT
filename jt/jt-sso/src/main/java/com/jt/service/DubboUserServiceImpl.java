package com.jt.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//该类是dubbo的实现类
@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster JedisCluster;
	
	@Transactional//添加事务控制
	@Override
	public void saveUser(User user) {
		//1.将密码加密
		String md5Pass = DigestUtils.md5Hex(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		//2.补齐入库数据 email暂时使用电话代替
		userMapper.insert(user);
	}
	/**
	 * 1.校验用户名或者密码是否正确
	 * 2.如果数据不正确，返回null
	 * 3.如果数据正确
	 * 		1.生成加密密钥：md5（JT_TICKER + username + 当前毫秒数）
	 * 		2.将userDB数据转化为userJSON
	 * 		3.将数据保存到redis中，7天超时
	 * 4.返回token
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		
		//1.将密码加密
		String md5Password = DigestUtils.md5Hex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		QueryWrapper<User> queryWrapper = new QueryWrapper(user);
		User userDB = userMapper.selectOne(queryWrapper);
		
		//2.判断是否正确,执行下列代码
		if(userDB != null) {
			token = "JT_TICKET" + userDB.getUsername() + System.currentTimeMillis();
			token = DigestUtils.md5Hex(token.getBytes());
			//脱敏处理,手机号，身份证密码等敏感信息 ，一切从业务出发
			userDB.setPassword("***");
			String userJSON = ObjectMapperUtil.toJSON(userDB);
			JedisCluster.setex(token, 60*60*24*7, userJSON);
		}
		return token;
	}
}
