package com.jt.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		List<Cart> cartList = cartMapper.selectList(queryWrapper);
		return cartList;
	}
	/**
	 * 更新信息：num/updated
	 * 判断条件:where user_id and item_id
	 */
	@Transactional
	@Override
	public void updateCartNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum())
				.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId())
					 .eq("item_id", cart.getItemId());
		cartMapper.update(tempCart, updateWrapper);
	}
	/**将对象中不为null的属性当作where条件
	 * 前提 ： 保证cart中只能有两个属性不为null
	 * 
	 */
	@Transactional
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
	}
	/**
	 * 新增业务实现：
	 * 1.用户第一次新增可以直接入库
	 * 2.用户不是第一次入库，应该只做数量的修改
	 * 
	 * 根据：itemId和userId查询数据
	 * 
	 * 衍生知识：
	 * 修改操作时，除了主键之外都要更新不为null的数据
	 // sql：update tb_cart set num = #{ num },updated = #{updated}....
	 *  where id = #{id}
	 * 目的：
	 * 	有时自己手写sql可能执行效率更好
	 * 
	 */
	@Transactional
	@Override
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//用户第一次购买商品，可以直接入库
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//表示用户多次添加购物车，只做数量修改
			int num = cart.getNum() + cartDB.getNum();
			cartDB.setNum(num)
				  .setUpdated(new Date());
			cartMapper.updateById(cartDB);
		}
	}
	

}
