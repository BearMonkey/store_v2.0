package com.itheima.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.service.impl.ProductServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

/**
 * 首页的Servlet:
 */
public class IndexServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String index(HttpServletRequest req,HttpServletResponse resp){
		try{
			// 查询最新商品:
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			List<Product> newList = productService.findByNew();
			// 查询热门商品:
			List<Product> hotList = productService.findByHot();
			req.setAttribute("newList", newList);
			req.setAttribute("hotList", hotList);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "/jsp/index.jsp";
	}
}
