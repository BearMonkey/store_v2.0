package com.itheima.store.web.servlet;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.UUIDUtils;

/**
 * 后台分类管理的Servlet
 */
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 后台查询所有分类的执行的方法:findAll
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		// 调用业务层
		CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
		try {
			List<Category> list = categoryService.findAll();
			// 存入req域中，完成页面跳转:
			req.setAttribute("list", list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "/admin/category/list.jsp";
	}
	
	/**
	 * 跳转到添加分类的页面执行的方法:saveUI
	 */
	public String saveUI(HttpServletRequest req,HttpServletResponse resp){
		return "/admin/category/add.jsp";
	}
	
	/**
	 * 保存分类的执行的方法:save
	 */
	public String save(HttpServletRequest req,HttpServletResponse resp){
		
		try {
			// 接收参数:
			String cname = req.getParameter("cname");
			// 封装数据:
			Category category = new Category();
			category.setCid(UUIDUtils.getUUID());
			category.setCname(cname);
			// 调用业务层:
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.save(category);
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 编辑分类的额执行的方法:edit
	 */
	public String edit(HttpServletRequest req,HttpServletResponse resp){
		
		try {
			// 接收参数:
			String cid = req.getParameter("cid");
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			Category category = categoryService.findById(cid);
			req.setAttribute("category", category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "/admin/category/edit.jsp";
	}
	
	/**
	 * 修改分类的执行的方法:update
	 */
	public String update(HttpServletRequest req,HttpServletResponse resp){
		try {
			// 接收参数:
			Map<String,String[]> map = req.getParameterMap();
			// 封装数据:
			Category category = new Category();
			BeanUtils.populate(category, map);
			// 处理数据:
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.update(category);
			// 页面跳转:
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除分类的执行的方法:delete
	 */
	public String delete(HttpServletRequest req,HttpServletResponse resp){
		try {
			String cid = req.getParameter("cid");
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
			categoryService.delete(cid);
			
			// 页面跳转:
			resp.sendRedirect(req.getContextPath()+"/AdminCategoryServlet?method=findAll");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
