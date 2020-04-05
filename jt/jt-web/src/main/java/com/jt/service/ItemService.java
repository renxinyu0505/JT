package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.pojo.User;

public interface ItemService {

	Item findItemById(Long itemId);

	ItemDesc findItemDescById(Long itemId);

	void saveUser(User user);


}
