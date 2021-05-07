package com.itheima.store.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;

/**
 * 订单模块的DAO的实现类
 * 
 * @author admin
 *
 */
public class OrderDaoImpl implements OrderDao {

	@Override
	public void saveOrder(Connection conn, Order order) throws SQLException {
        String sql = "insert into orders(oid, ordertime, total, state, address, name, telephone, uid) values(?,?,?,?,?,?,?,?)";
	    PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, order.getOid());
            ps.setTimestamp(2, order.getOrdertime());
            ps.setDouble(3, order.getTotal());
            ps.setInt(4, order.getState());
            ps.setString(5, order.getAddress());
            ps.setString(6, order.getName());
            ps.setString(7, order.getTelephone());
            ps.setString(8, order.getUser().getUid());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
	    
		/*QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orders(oid, ordertime total, state, address, name, telephone, uid) values(?,?,?,?,?,?,?,?)";
		Object[] params = { order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(),
				order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid() };
		System.out.println(order);
		queryRunner.update(conn, sql, params);*/
	}

	@Override
	public void saveOrderItem(Connection conn, OrderItem orderItem) throws SQLException {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orderitem values (?,?,?,?,?)";
		Object[] params = { orderItem.getItemid(), orderItem.getCount(), orderItem.getSubtotal(),
				orderItem.getProduct().getPid(), orderItem.getOrder().getOid() };
		queryRunner.update(conn, sql, params);
	}

	@Override
	public Integer findCountByUid(String uid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from orders where uid = ?";
		Long count =  (Long) queryRunner.query( sql, new ScalarHandler(), uid);
		return count.intValue();
	}

	@Override
	public List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where uid = ? order by ordertime desc limit ?,?";
		List<Order> list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class), uid,begin,pageSize);
		for (Order order : list) {
			sql = "SELECT * FROM orderitem o,product p WHERE o.pid = p.pid AND o.oid = ?";
			List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(), order.getOid());
			// 遍历LIst的Map集合:获取每条记录，封装到不同的对象中.
			for (Map<String, Object> map : oList) {
				Product product = new Product();
				//product.setPname(map.get("pname"));
				BeanUtils.populate(product, map);
				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				
				orderItem.setProduct(product);
				
				order.getOrderItems().add(orderItem);
			}
		}
		return list;
	}

	@Override
	public Order findByOid(String oid) throws Exception {
	    String sql = "SELECT o.oid oid, o.ordertime ordertime, o.total total, o.state state, o.address address, "
	            + "o.name name, o.telephone telephone, o.uid uid "
	            + "FROM orders o WHERE o.oid = ?";
        Order order = new Order();
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, oid);
            rs = ps.executeQuery();
            if (rs.next()) {
                order.setOid(rs.getString("oid"));
                java.sql.Date sqlDate = rs.getDate("ordertime");
                order.setOrdertime(sqlDate == null ? null : new Timestamp(sqlDate.getTime()));
                order.setTotal(rs.getDouble("total"));
                order.setState(rs.getInt("state"));
                String address = rs.getString("address");
                order.setAddress(address == null ? null : address);
                String name = rs.getString("address");
                order.setName(name == null ? null : name);
                String telephone = rs.getString("address");
                order.setTelephone(telephone == null ? null : telephone);
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
	    sql = "SELECT i.itemid itemid, i.count count, i.subtotal subtotal, " + 
	          "       p.pid pid, p.pname pname, p.market_price marketPrice, p.shop_price shopPrice, " + 
	          "       p.pdate pdate, p.is_hot isHot, p.pdesc pdesc, p.pflag pflag, p.cid cid " + 
	          "FROM   orderitem i, product p " + 
	          "WHERE  i.oid = ? " + 
	          "AND    i.pid = p.pid";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, oid);
            rs = ps.executeQuery();
            List<OrderItem> itemList = new ArrayList<OrderItem>();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                Product product = new Product();
                item.setItemid(rs.getString("itemid"));
                item.setCount(rs.getInt("count"));
                item.setSubtotal(rs.getDouble("subtotal"));
                product.setPid(rs.getString("pid"));
                product.setPname(rs.getString("pname"));
                product.setMarket_price(rs.getDouble("marketPrice"));
                product.setShop_price(rs.getDouble("shopPrice"));
                java.sql.Date date = rs.getDate("pdate");
                product.setPdate(date == null ? null : new Date(date.getTime()));
                product.setIs_hot(rs.getInt("isHot"));
                product.setPdesc(rs.getString("pdesc"));
                product.setPflag(rs.getInt("pflag"));
                item.setProduct(product);
                itemList.add(item);
            }
            order.setOrderItems(itemList);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            conn.close();
        }
        return order;
		/*QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where oid = ?";
		Order order = queryRunner.query(sql, new BeanHandler<Order>(Order.class), oid);
		sql = "SELECT * FROM orderitem o,product p WHERE o.pid = p.pid AND o.oid = ?";
		List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(), oid);
		for (Map<String, Object> map : oList) {
			Product product = new Product();
			BeanUtils.populate(product, map);
			
			OrderItem orderItem = new OrderItem();
			BeanUtils.populate(orderItem, map);
			
			orderItem.setProduct(product);
			
			order.getOrderItems().add(orderItem);
		}
		return order;*/
	}

	@Override
	public void update(Order order) throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update orders set total = ?,state = ? ,address = ?,name=?,telephone = ? where oid = ?";
		Object[] params = {order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone() ,order.getOid()};
		queryRunner.update(sql,params);
	}

	@Override
	public List<Order> findAll() throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders order by ordertime desc";
		List<Order> list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class));
		return list;
	}

	@Override
	public List<Order> findByState(int pstate) throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where state = ? order by ordertime desc";
		List<Order> list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),pstate);
		return list;
	}

	@Override
	public List<OrderItem> showDetail(String oid) throws Exception {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "SELECT * FROM orderitem o,product p WHERE o.pid = p.pid AND o.oid = ?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(),oid);
		for (Map<String, Object> map : oList) {
			Product product = new Product();
			BeanUtils.populate(product, map);
			
			OrderItem orderItem = new OrderItem();
			BeanUtils.populate(orderItem, map);
			
			orderItem.setProduct(product);
			
			list.add(orderItem);
		}
		return list;
	}

}
