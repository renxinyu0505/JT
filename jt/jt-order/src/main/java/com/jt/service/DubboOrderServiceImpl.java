package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	/**
	 * 一个业务逻辑需要操作三张表
	 * 1.添加事务控制
	 * 2.表数据分析 order orderItems orderShipping
	 * 3.orderId “订单号：登陆用户ID + 当前时间戳”
	 * 4.操作三个mapper分别入库
	 */
	@Transactional
	@Override
	public String insertOrder(Order order) {
		//1.获取orderId
		String orderId = ""+order.getUserId() + System.currentTimeMillis();
		Date date = new Date();
		//2.入库订单
		order.setOrderId(orderId)
			 .setStatus(1)
			 .setCreated(date)
			 .setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功");
		//3.入库订单物流
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("订单物流入库成功");
		//4.入库订单商品 1.自己遍历集合分别入库 2.自己编辑语句批量入库
		//insert into order_items(xxx,xxx) values (xxx,xxx),(xxx,xxx)...
		List<OrderItem> orderLists = order.getOrderItems();
		for(OrderItem item : orderLists) {
			item.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
			orderItemMapper.insert(item);
		}
		System.out.println("订单商品入库成功");
		
		return orderId;
	}

	/**
	 * 查询三张表
	 */
	@Override
	public Order findOrderById(String id) {
//		Order order = orderMapper.selectById(id);
//		OrderShipping shipping = orderShippingMapper.selectById(id);
//		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("order_id", id);
//		List<OrderItem> list = orderItemMapper.selectList(queryWrapper);
//		order.setOrderItems(list)
//			 .setOrderShipping(shipping);
//		return order;
		Order order = orderMapper.selectById(id);
		OrderShipping shipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
		
		return order.setOrderShipping(shipping)
					.setOrderItems(orderItems);
	}
}
