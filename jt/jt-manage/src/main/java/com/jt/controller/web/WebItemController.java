package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")
public class WebItemController {
	@Autowired
	private ItemService itemService;
	//后台接收用户请求获取商品信息
	//url ：manage.jt.com/web/item/findItemById?id=xxx
	@RequestMapping("/findItemById")
	public Item findItemById(Long id) {
		return itemService.findItemById(id);
	}
	//商品详情
	@RequestMapping("/findItemDescById")
	public ItemDesc findItemDescById(Long id) {
		
		return itemService.findItemDescById(id);
	}
}
