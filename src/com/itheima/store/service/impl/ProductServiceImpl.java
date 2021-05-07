package com.itheima.store.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.itheima.store.dao.ProductDao;
import com.itheima.store.dao.impl.ProductDaoImpl;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
/**
 * 商品Service的实现类
 * @author admin
 *
 */
public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> findByHot()throws SQLException  {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		return productDao.findByHot();
	}

	@Override
	public List<Product> findByNew()throws SQLException  {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		return productDao.findByNew();
	}

	@Override
	public PageBean<Product> findByPageCid(String cid, Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>();
		// 设置参数：
		// 设置当前页数：
		pageBean.setCurrPage(currPage);
		// 设置每页显示的记录数:
		Integer pageSize = 12;
		pageBean.setPageSize(pageSize);
		// 设置总记录数:
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		Integer totalCount = productDao.findCountByCid(cid);
		pageBean.setTotalCount(totalCount);
		// 设置总页数:
		double tc = totalCount;
		Double num = Math.ceil(tc / pageSize);
		pageBean.setTotalPage(num.intValue());
		// 设置每页显示的数据的集合:
		int begin = (currPage - 1) * pageSize;
		List<Product> list = productDao.findPageByCid(cid,begin,pageSize);
		pageBean.setList(list);
		return pageBean;
	}

	@Override
	public Product findByPid(String pid) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		return productDao.findByPid(pid);
	}

	@Override
	public PageBean<Product> findByPage(Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>();
		// 设置参数：
		// 设置当前页数：
		pageBean.setCurrPage(currPage);
		// 设置每页显示的记录数:
		Integer pageSize = 10;
		pageBean.setPageSize(pageSize);
		// 设置总记录数:
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		Integer totalCount = productDao.findCount();
		pageBean.setTotalCount(totalCount);
		// 设置总页数:
		double tc = totalCount;
		Double num = Math.ceil(tc / pageSize);
		pageBean.setTotalPage(num.intValue());
		// 设置每页显示的数据的集合:
		int begin = (currPage - 1) * pageSize;
		List<Product> list = productDao.findByPage(begin,pageSize);
		pageBean.setList(list);
		return pageBean;
	}

	@Override
	public void save(Product product) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		productDao.save(product);
	}

	@Override
	public void update(Product product) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		productDao.update(product);
	}

	@Override
	public List<Product> findByPushDown() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
		return productDao.findByPushDown();
	}

}
