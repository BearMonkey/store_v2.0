package com.itheima.store.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.PageBean;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.JDBCUtils;
/**
 * 订单模块的Service的实现类：
 * @author admin
 *
 */
public class OrderServiceImpl implements OrderService {

	@Override
	public void save(Order order) {
		Connection conn = null;
		try{
			// 开启事务:
			conn = JDBCUtils.getConnection();
			conn.setAutoCommit(false);
			// 执行保存操作:
			OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
			orderDao.saveOrder(conn,order);
			// 循环保存订单中的订单项:
			for(OrderItem orderItem : order.getOrderItems()){
				orderDao.saveOrderItem(conn,orderItem);
			}
			
			// 提交事务:
			DbUtils.commitAndCloseQuietly(conn);
		}catch(Exception e){
			e.printStackTrace();
			DbUtils.rollbackAndCloseQuietly(conn);
		}
	}

	@Override
	public PageBean<Order> findByUid(String uid, Integer currPage)  throws Exception{
		PageBean<Order> pageBean = new PageBean<Order>();
		// 设置当前的页数：
		pageBean.setCurrPage(currPage);
		// 设置每页显示的记录数:
		Integer pageSize = 5;
		pageBean.setPageSize(pageSize);
		// 设置总记录数:
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		Integer totalCount = orderDao.findCountByUid(uid);
		pageBean.setTotalCount(totalCount);
		// 设置总页数
		double tc = totalCount;
		Double num = Math.ceil(tc / pageSize);
		pageBean.setTotalPage(num.intValue());
		// 设置每页显示的数据的集合:
		int begin = (currPage - 1)* pageSize;
		List<Order> list = orderDao.findPageByUid(uid,begin,pageSize);
		pageBean.setList(list);
		return pageBean;
	}

	@Override
	public Order findByOid(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findByOid(oid);
	}

	@Override
	public void update(Order order) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		orderDao.update(order);
	}

	@Override
	public List<Order> findAll() throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findAll();
	}

	@Override
	public List<Order> findByState(int pstate) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.findByState(pstate);
	}

	@Override
	public List<OrderItem> showDetail(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
		return orderDao.showDetail(oid);
	}

}
