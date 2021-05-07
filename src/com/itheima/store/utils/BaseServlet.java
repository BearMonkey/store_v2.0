package com.itheima.store.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 通用的SErvlet的编写:
 * @author admin
 *
 */
public class BaseServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// http://localhost:8080/store_v2.0/UserSErvlet?method=regist
		// 接收参数:
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String methodName = req.getParameter("method");
		if(methodName == null || "".equals(methodName)){
			resp.getWriter().println("method参数为null!!!");
			return;
		}
		// 获得子类的Class对象：
		Class clazz = this.getClass();
		// 获得子类中的方法了:
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			// 使方法执行:
			Object result = method.invoke(this, req,resp);
            String contentType = resp.getContentType();
			System.out.println("url: " +req.getRequestURI()+ ", method: " + methodName 
			        + ", contentType: " + contentType + ", result: " + result);
			
			if (!resp.getContentType().contains("application/json")) {
                String path = (String) result;
                System.out.println("RequestDispatcher: " + path);
                if (req != null) {
                    RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
                    if (requestDispatcher == null) {
                        throw new Exception("path is null;");
                    }
                    requestDispatcher.forward(req, resp);
                }
			} else {
			    Result tmp = (Result) result;
			    if (tmp.getRespCode().equals("9999")) {
			        req.setAttribute("errorInfo", tmp.getMsg());
			        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/jsp/regist.jsp");
			    }
			    ObjectMapper mapper = new ObjectMapper();
			    resp.getWriter().print(mapper.writeValueAsString(result));
			}
		} catch (Exception e) {
			// e.printStackTrace();
		    System.out.println("exception: " + e.getMessage());
		}
	}
	
	
}
