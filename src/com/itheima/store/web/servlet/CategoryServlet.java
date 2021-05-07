package com.itheima.store.web.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.service.ProductService;
import com.itheima.store.service.impl.CategoryServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.Result;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class CategoryServlet
 */
public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 查询所有分类的Servlet的执行的方法:findAll
	 */
	public Result findAll(HttpServletRequest req,HttpServletResponse resp){
	    System.out.println("CategoryServlet findAll");
		// 调用业务层:
	    List<Category> list = new ArrayList<Category>();
		try{
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			list = categoryService.findAll();
			System.out.println("CategoryServlet findAll size=" + list.size());
			// 将list转成JSON: 
			// JSONArray jsonArray = JSONArray.fromObject(list);
			// System.out.println("Category json: " + jsonArray.toString());
			
			// resp.getWriter().println(jsonArray.toString());
		} catch(Exception e){
		    System.out.println("CategoryServlet findAll exception.");
			e.printStackTrace();
		}
        resp.setContentType("application/json");
		return new Result("0", "success", list);
	}
}
