package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.ItemService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
//	@Autowired
//	private ItemService service;
	@Autowired
	private JedisCluster jedisCluster;
	
	//导入dubbo的用户接口
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;

	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName) {

		return moduleName;
	}
	//使用dubbo形式实现业务调用
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		}catch(Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**实现用户登陆
	 * 说明：利用Response对象将cookie数据写入客户端
	 * cookie生命周期
	 * cookie.setMaxAge(0) ; 立即删除
	 * cookie.setMaxAge(值>0);能够存活多久 /秒
	 * cookie.setMaxAge(值<0);表示当会话结束后删除
	 * 
	 * cookie.setPath("/");表示cookie的权限
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse response) {
		try {
			//调用sso系统获取密钥
			String token = userService.findUserByUP(user);
			//判断数据是否正确,如果不为null ，将数据保存在cookie中
			//cookie中的key固定值JT_TICKET
			if( !StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET",token);
				cookie.setMaxAge(7*24*60*60);//生命周期
				cookie.setPath("/");
				cookie.setDomain("jt.com");//实现数据的共享
				response.addCookie(cookie);
				return SysResult.ok();
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return SysResult.fail();
	}
	
	/**实现用户登出操作
	 * 1.删除redis request对象 cookie中 JT_TICKET
	 * 2.删除cookie
	 * @param request
	 */
	@RequestMapping("/logout")
	public String logOut(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies.length!=0) {
			String token = null;
			for(Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
				//判断token是否有值
			if(!StringUtils.isEmpty(token)) {
				//删除redis
				jedisCluster.del(token);
				//删除cookie,value不能为空， 可以为空串
				Cookie cookie = new Cookie("JT_TICKET","");
				cookie.setMaxAge(0);//立即删除cookie
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				response.addCookie(cookie);
			}
		}
		//当用户登出时，页面重定向到系统首页
		return "redirect:/";
	}
	
	
}
