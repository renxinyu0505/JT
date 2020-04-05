package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

@Component //将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4的版本中要求必须重写3个方法，不管是否需要使用
	 * 在spring5的版本中接口调价default属性，则省略不用必须重写
	 */

	/**
	 * 返回值结果 
	 * true ： 拦截放行
	 * false：请求拦截，重定向到登陆页面
	 * 
	 * 业务逻辑：
	 * 1.获取Cookie数据
	 * 2.从Cookie中获取token(JT_TICKET)
	 * 3.判断redis缓存服务器中是否有数据
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		//1.获取Cookie信息
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		
		//2.判断token是否有效
		if(!StringUtils.isEmpty(token)) {
			//4.判断redis中是否有用户数据
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis中有用户数据
//				//将userJSON转化为user对象
//				User user = ObjectMapperUtil.toObject(userJSON, User.class);
//				//将数据存入request中
//				request.setAttribute("JT_USER", user);
				//使用threadlocal来做
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				UserThreadLocal.set(user);
				return true;
			}
		}

		//3.重定向到用户登陆页面
		response.sendRedirect("/user/login.html");
		return false;//表示拦截

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
