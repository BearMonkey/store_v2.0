package com.itheima.store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.store.dao.ProductDao;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;

/**
 * 商品DAO的实现类
 * 
 * @author admin
 *
 */
public class ProductDaoImpl implements ProductDao {

	@Override
	public List<Product> findByHot() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? and is_hot = ? order by pdate desc limit ?";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, 1, 9);
		return list;
	}

	@Override
	public List<Product> findByNew() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? order by pdate desc limit ?";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
		return list;
	}

	@Override
	public Integer findCountByCid(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from product where cid = ? and pflag = ?";
		Long count = (Long) queryRunner.query(sql, new ScalarHandler(), cid, 0);
		return count.intValue();
	}

	@Override
	public List<Product> findPageByCid(String cid, int begin, Integer pageSize) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? and cid = ? order by pdate desc limit ?,?";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, cid, begin,
				pageSize);
		return list;
	}

	@Override
	public Product findByPid(String pid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pid = ?";
		Product product = queryRunner.query(sql, new BeanHandler<Product>(Product.class), pid);
		return product;
	}

	@Override
	public Integer findCount() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from product where pflag = ?";
		Long count = (Long) queryRunner.query(sql, new ScalarHandler(), 0);
		return count.intValue();
	}

	@Override
	public List<Product> findByPage(int begin, Integer pageSize) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? order by pdate desc limit ?,?";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, begin, pageSize);
		return list;
	}

	@Override
	public void save(Product product) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into product values (?,?,?,?,?,?,?,?,?,?)";
		Object[] params = { product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(), product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(),
				product.getCategory().getCid() };
		queryRunner.update(sql, params);
	}

	@Override
	public void update(Product product) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update product set pname = ?,market_price=?,shop_price=?,pimage=?,is_hot=?,pdesc= ?,pflag=? where pid = ?";
		Object[] params = { product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(),product.getIs_hot(), product.getPdesc(), product.getPflag(),product.getPid()
				 };
		queryRunner.update(sql, params);
	}

	@Override
	public List<Product> findByPushDown() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from product where pflag = ? order by pdate desc";
		List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 1);
		return list;
	}

}
