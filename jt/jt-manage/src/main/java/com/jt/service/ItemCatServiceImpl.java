package com.jt.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;


@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	private ItemCatMapper itemCatMapper;
//	@Autowired
//	private Jedis jedis;

	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat i = itemCatMapper.selectById(itemCatId);
		String name = i.getName();
		
		return name;
	}
	/**
	 * 根据parentid查询数据库记录返回itemCatList数据
	 * 将itemCatList集合中的数据按照指定的格式封装成
	 * List<EasyUITree>
	 */
	@Override
	public List<EasyUITree> findItemCatById(Long parentId) {
		List<ItemCat> cartList = findItemCatList(parentId);
		List<EasyUITree> treeList = new ArrayList<>();
		//遍历集合数据，实现数据转换
		for(ItemCat i : cartList) {
			EasyUITree uiTree = new EasyUITree();
			uiTree.setId(i.getId());
			uiTree.setText(i.getName());
			//是否是父级元素
			String state = i.getIsParent()?"closed" : "open";
			uiTree.setState(state);
			treeList.add(uiTree);
		}
		return treeList;
	}
	
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> qw = new QueryWrapper<>();
		qw.eq("parent_id", parentId);
		return itemCatMapper.selectList(qw);
	}
	/*
	 * 引入了AOP切面
	@Override
	public List<EasyUITree> findItemCatByCache(Long parentId) {
		String key = "ITEM_CAT_" + parentId;
		String result = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList();
		if(StringUtils.isEmpty(result)) {
			//如果为null ， 查询数据库
			treeList = findItemCatById(parentId);
			//将数据转化为JSON
			String JSON = ObjectMapperUtil.toJSON(treeList);
			jedis.setex(key, 7 * 24 * 3600 , JSON);
			System.out.println("业务查询数据库");
		}else {
			//表示缓存中有数据
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("业务查询redis缓存");
		}
		return treeList;
	}
	*/
}
