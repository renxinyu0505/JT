package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.pojo.User;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClient;

	@Override
	public Item findItemById(Long itemId) {
		//manage.jt.com/web/item/findItemById?id=xxx
		String url = "http://manage.jt.com/web/item/findItemById";
		//为了满足get请求，定义id=xxx
		Map<String,String> params = new HashMap<>();
		params.put("id", itemId+"");
		String result = httpClient.doGet(url,params);
		Item item = ObjectMapperUtil.toObject(result, Item.class);
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		String url = "http://manage.jt.com/web/item/findItemDescById";
		//为了满足get请求，定义id=xxx
		Map<String,String> params = new HashMap<>();
		params.put("id", itemId+"");
		String result = httpClient.doGet(url,params);
		ItemDesc itemDesc = ObjectMapperUtil.toObject(result, ItemDesc.class);
		return itemDesc;
	}

	@Override
	public void saveUser(User user) {
		String url = "http://www.sso.com/user/doRegister";
		
		//密码加密
		String md5Pass = DigestUtils.md5Hex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		String userJson = ObjectMapperUtil.toJSON(user);
		Map<String,String> params = new HashMap<>();
		params.put("userJSON", userJson);
		String result = httpClient.doPost(url, params);
		
		
		}

}
