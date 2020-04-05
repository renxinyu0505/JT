package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	/**
	 * 1.用户发起post请求 携带了itemCatid=560
	 * 2.servlet
	 * @return
	 */
	//实现根据id查询商品分类信息
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {

		return itemCatService.findItemCatNameById(itemCatId);
	}

	//@RequestParam()  value 为传到后台的值的key 接收参数的名称
	//required = true/false 是否必须传值
	@RequestMapping("/list")
	@Cache_Find(key="ITEM_CAT",keyType = KEY_ENUM.AUTO,seconds = 0)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value ="id",defaultValue = "0")Long parentId){
		
		return itemCatService.findItemCatById(parentId);
		
//		return itemCatService.findItemCatByCache(parentId);
	}
}
