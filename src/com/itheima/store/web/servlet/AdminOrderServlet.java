package com.itheima.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * Servlet implementation class AdminOrderServlet
 */
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public String findAll(HttpServletRequest req,HttpServletResponse resp){
		// 接收状态:
		try{
			String state = req.getParameter("state");
			OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
			List<Order> list = null;
			if(state == null){
				// 查询所有订单:
				list = orderService.findAll();
			}else{
				// 按状态查询订单:
				int pstate = Integer.parseInt(state);
				list = orderService.findByState(pstate);
			}
			req.setAttribute("list", list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/admin/order/list.jsp";
	}
	
	public String showDetail(HttpServletRequest req,HttpServletResponse resp){
		// 接收oid:
		String oid = req.getParameter("oid");
		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
		try {
			List<OrderItem> list = orderService.showDetail(oid);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"order"});
			JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
			
			resp.getWriter().println(jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String send(HttpServletRequest req,HttpServletResponse resp){
		// 接收oid:
		try{
		String oid = req.getParameter("oid");
		OrderService orderService = (OrderService) BeanFactory.getBean("orderService");
		Order order = orderService.findByOid(oid);
		order.setState(3);
		orderService.update(order);
		resp.sendRedirect(req.getContextPath()+"/AdminOrderServlet?method=findAll");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
