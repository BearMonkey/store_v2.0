package com.itheima.store.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;

/**
 * 订单模块的DAO的接口
 * @author admin
 *
 */
public interface OrderDao {

	void saveOrder(Connection conn, Order order) throws SQLException;

	void saveOrderItem(Connection conn, OrderItem orderItem) throws SQLException;

	Integer findCountByUid(String uid) throws SQLException;

	List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception;

	Order findByOid(String oid)throws Exception;

	void update(Order order)throws Exception;

	List<Order> findAll()throws Exception;

	List<Order> findByState(int pstate)throws Exception;

	List<OrderItem> showDetail(String oid)throws Exception;

}
