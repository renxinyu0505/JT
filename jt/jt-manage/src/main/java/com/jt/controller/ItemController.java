package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	//http://localhost:8091/item/query?page=1&rows=20
	//查询商品列表信息 分页查询
	@RequestMapping("/query")
	public EasyUIData findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
		
	}
	
	//新增
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.saveItem(item,itemDesc);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.updateItem(item,itemDesc);
			return SysResult.ok();
		}catch(Exception e) {
			e.printStackTrace();
			return SysResult.fail("商品修改失败");
		}
	}
	//删除
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		try {
			itemService.deleteItem(ids);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//上架下架
	@RequestMapping("instock")
	public SysResult instock(Long[] ids) {
		try {
			int status = 2;//下架
			itemService.updateStatus(ids,status);
			return SysResult.ok();
		}catch(Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	
	//上架下架
		@RequestMapping("/reshelf")
		public SysResult reshelf(Long[] ids) {
			try {
				int status = 1;//上架
				itemService.updateStatus(ids,status);
				return SysResult.ok();
			}catch(Exception e) {
				e.printStackTrace();
				return SysResult.fail();
			}
		}
		/**
		 * 编辑时回显数据
		 * @param itemId
		 * @return
		 */
		///item/param/item/query/'+data.id
		@RequestMapping("/query/item/desc/{itemId}")
		public SysResult findItemDescById(@PathVariable(name = "itemId") Long itemId) {
			try {
				ItemDesc itemDesc = itemService.findItemDescById(itemId);
				return SysResult.ok(itemDesc);
			}catch(Exception e) {
				e.printStackTrace();
				return SysResult.fail();
			}
			
		}
}
