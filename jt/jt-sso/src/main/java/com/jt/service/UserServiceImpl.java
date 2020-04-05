package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**true 表示用户已经存在 false 表示用户可以使用
	 * 1.param 用户参数
	 * 2.type   1 username/2 phone/3 email
	 * 	 
	 * 将type转化为具体字段
	 * 
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		String column = type == 1 ? "username" : (type == 2 ? "phone" : "email" );
		
//		switch(type) {
//		case 1:
//			column = "username";
//			break;
//		case 2:
//			column = "phone";
//			break;
//		case 3:
//			column = "email";
//			break;
//		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		int count = userMapper.selectCount(queryWrapper);
		
		return count == 0 ? false : true;
	
	}
	@Transactional
	public void saveUser(User user) {
		user.setEmail(user.getEmail())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		userMapper.insert(user);
	}
	
	
	
}
