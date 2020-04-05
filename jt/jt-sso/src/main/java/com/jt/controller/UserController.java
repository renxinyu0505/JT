package com.jt.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 业务说明
	 * 校验用户是否存在
	 * http://sso.jt.com/user/check/sdsadasdsad/1?r=0.7986087831388553&callback=jsonp1583833359464&_=1583833577652
	 * 返回值：SysResult
	 * 由于跨域请求，所以返回值必须特殊处理 callback(json)
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								@PathVariable Integer type,
								String callback) {
		
		JSONPObject object = null;
		try {
			boolean flag = userService.checkUser(param,type);
			
			object = new JSONPObject(callback,SysResult.ok(flag));
		}catch(Exception e) {
			e.printStackTrace();
			object= new JSONPObject(callback,SysResult.fail());
		}
		 
		
		return object;
	}
	
	@RequestMapping("/doRegister")
	public SysResult saveUser(String userJSON) {
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		}catch(Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//利用跨域实现用户信息回显
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			return new JSONPObject(callback, SysResult.fail());
		}else {
			return new JSONPObject(callback, SysResult.ok(userJSON));
		}
		//回传数据需要经过200判断 SysResult对象
	}
	
	
	
}
