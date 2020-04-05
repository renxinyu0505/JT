package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {
		
		//1.获取商品记录总数
		int total = itemMapper.selectCount(null);
		
		//2.分页之后回传数据
		/**
		 * sql : select * from tb_item limit 起始位置，查询行数
		 * 第一页
		 * select * from tb_item limit 0,20
		 * 第二页
		 * select * from tb_item limit 20,20
		 * 第三页
		 * select * from tb_item limit 40,20
		 * 第n页
		 * select * from tb_item limit (page-1)*rows,rows
		 */
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		
		return new EasyUIData(total,itemList);
	}
	
	//新增商品
	@Transactional//添加事务控制
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		itemMapper.insert(item);
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
		
	}
	
	//根据主键更新
	/**
	 * 关于事务控制的说明：
	 * @Transactional 说明
	 * propagation 事务的传播属性 枚举类  
	 * 	默认值REQUIRED 必须添加事务
	 * 	REQUIRES_NEW 必须新建一个事务
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		//同时修改两张表数据
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}
	@Transactional
	@Override
	public void deleteItem(Long[] ids) {
		//mybatis手动写
//		itemMapper.deleteItem(ids);
		//mybatis-plus
		List<Long> itemList  = Arrays.asList(ids);
		itemMapper.deleteBatchIds(itemList);
		itemDescMapper.deleteBatchIds(itemList);
		
	}
	
	//update tb_item set status =#{status},updated=#{updated} where id in(
	@Transactional
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status)
			.setUpdated(new Date());
		UpdateWrapper<Item> uw = new UpdateWrapper<>();
		List<Long> list = Arrays.asList(ids);
		uw.in("id", list);
		itemMapper.update(item, uw);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}
	
	//查询item对象
	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}
	
	
	
	
	
	
	
}
