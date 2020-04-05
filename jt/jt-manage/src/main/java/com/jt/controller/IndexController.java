package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *	restFul 风格/结构 :
 *1.要求参数必须使用/分割
 *2.参数位置必须固定
 *3.接收参数时必须使用特定的注解，最好名称一致 
 *
 */
@Controller
public class IndexController {
	
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
}
