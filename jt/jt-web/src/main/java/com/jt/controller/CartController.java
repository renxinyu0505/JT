package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

//因为需要跳转页面，不能用restController
//如果返回值是json，在方法上加responsebody注解
@Controller
@RequestMapping("/cart")
public class CartController {
	/**
	 * 1.实现商品信息展现
	 * 2.页面取值：${cartList}
	 */
	@Reference
	private DubboCartService cartService;
	
	
	@RequestMapping("/show")
	public String findCartList(Model model) {
//		User user = (User)request.getAttribute("JT_USER");
//		Long userId = user.getId(); 
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		
		return "cart";//返回页面
	}
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			Long userId = UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		}catch(Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	/**
	 * 实现购物车删除操作
	 * restFul 传递的itemId  被 Cart cart 形参接收
	 * cart.setUserId(userId); 之后 cart里有两个属性 userId 和 itemId
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到购物车列表页
		return "redirect:/cart/show.html";
	}
	/**
	 * 实现添加购物车操作
	 * 页面表单提交 发起post请求 携带购物车参数
	 * 跳转到购物车展现页面
	 */
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//新增数据后，展现购物车列表页面
		return "redirect:/cart/show.html";
	}
}
