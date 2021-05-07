package com.itheima.store.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.service.impl.UserServiceImpl;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.MyDateConverter;
import com.itheima.store.utils.Result;
import com.itheima.store.utils.StringUtils;

/**
 * 用户模块的Servlet:
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 跳转到注册页面的执行的方法：registUI
	 */
	public String registUI(HttpServletRequest req,HttpServletResponse resp){
		return "/jsp/regist.jsp";
	}
	
	/**
	 * 异步校验用户名的执行的方法: checkUsername
	 */
	public Result checkUserName(HttpServletRequest req,HttpServletResponse resp){
	    System.out.println("checkUserName");
        resp.setContentType("application/json");
		try{
			// 接收参数:
			String username = req.getParameter("username");
            if (username == null || "".equals(username.trim())) {
                return new Result("1002", "username is null", null);
            }
			// 调用业务层:
			UserService userService = (UserService) BeanFactory.getBean("userService");
			User existUser = userService.findByUsername(username);
			if(existUser != null){
                // 用户名已经存在:
			    return new Result("1003", "username has exist", null);
			}else{
                // 用户名可以使用:
                return new Result();
			}
		}catch(Exception e){
		    e.printStackTrace();
		}
		return null;
	}

    /**
                * 异步校验密码
     * @param req
     * @param resp
     * @return
     */
    public Result checkPassword(HttpServletRequest req,HttpServletResponse resp){
        System.out.println("checkPassword");
        String password = (String) req.getParameter("password");
        resp.setContentType("application/json");
        if (password == null || "".equals(password.trim())) {
            return new Result("1000", "password is null", null);
        }
        return new Result();
    }

    public Result registCheck(HttpServletRequest req,HttpServletResponse resp) {
        System.out.println("========registCheck"); 
        resp.setContentType("application/json");
        
        // 接收参数:
        String username = req.getParameter("username");
        String password = (String) req.getAttribute("passwd");
        String confirmPassword = (String) req.getAttribute("repassword");
        
        if (username == null) {
            return new Result("1002", "username is null", null);
        }
        if (password == null) {
            return new Result("1000", "password is null", null);
        }
        if (confirmPassword == null) {
            return new Result("1001", "confirm password is null", null);
        }
        
        if (!password.equalsIgnoreCase(confirmPassword)) {
            return new Result("1004", "passwod != confirmPassword", null);
        }
        
        try {
            // 调用业务层:
            UserService userService = (UserService) BeanFactory.getBean("userService");
            User existUser;
            existUser = userService.findByUsername(username);
            if(existUser != null){
                // 用户名已经存在:
                return new Result("1003", "username has exist", null);
            }
        } catch (SQLException e) {
            return new Result("9999", "unknown error", e);
        }
        return new Result();
    }

    /**
                * 异步校验密码
     * @param req
     * @param resp
     * @return
     */
    public Result checkConfirmPassword(HttpServletRequest req,HttpServletResponse resp){
        System.out.println("checkConfirmPassword");
        resp.setContentType("application/json");
        String password = (String) req.getParameter("password");
        String confirmPwd = (String) req.getParameter("confirmPwd");
        if (password == null || "".equals(password.trim())) {
            return new Result("1000", "password is null", null);
        }
        if (confirmPwd == null || "".equals(confirmPwd.trim())) {
            return new Result("1001", "password is null", null);
        }
        if (!password.equalsIgnoreCase(confirmPwd)) {
            return new Result("1004", "passwod != confirmPassword", null);
        }
        return new Result();
    }
	
    /**
                *        异步校验验证码
     * @param req
     * @param resp
     * @return
     */
    public Result checkCode(HttpServletRequest req,HttpServletResponse resp) {
        System.out.println("checkCode");
        resp.setContentType("application/json");
        String code1 = req.getParameter("code");
        String code2 = (String)req.getSession().getAttribute("code");
        if (StringUtils.isEmpty(code1)) {
            return new Result("1005", "code is null", null);
        }
        if (StringUtils.isEmpty(code2)) {
            return new Result("1007", "code is timeout", null);
        }
        if (!code1.equalsIgnoreCase(code2)) {
            return new Result("1006", "code is error", null);
        }
        return new Result();
    }
    
	/**
	 * 用户注册的执行的方法:regist
	 */
	public String regist(HttpServletRequest req,HttpServletResponse resp){
		try{
			// 接收数据:
			Map<String,String[]> map = req.getParameterMap();
			// 封装数据:
			User user = new User();
			ConvertUtils.register(new MyDateConverter(), Date.class);
			BeanUtils.populate(user, map);
			// 调用业务层:
			UserService userService = (UserService) BeanFactory.getBean("userService");
			user.setState(2); // 没有邮件服务器，直接修改为激活状态
			userService.save(user);
			// 页面跳转:
			req.setAttribute("msg", "注册成功！");
			return "/jsp/msg.jsp";
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * 用户激活的方法:
	 * @param req
	 * @param resp
	 * @return
	 */
	public String active(HttpServletRequest req,HttpServletResponse resp){
		// 接收激活码：
		String code = req.getParameter("code");
		// 根据激活码查询:
		UserService userService = (UserService) BeanFactory.getBean("userService");
		User existUser;
		try {
			existUser = userService.findByCode(code);
			// 判断
			if(existUser == null){
				// 激活码有误:
				req.setAttribute("msg", "激活码错误！请重新激活！");
			}else{
				// 激活操作:
				existUser.setState(2); // 设置激活的状态为已经激活.
				existUser.setCode(null);
				userService.update(existUser);
				req.setAttribute("msg", "激活成功！请去登录！");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 跳转到登录页面的执行的方法:loginUI
	 */
	public String loginUI(HttpServletRequest req,HttpServletResponse resp){
		return "/jsp/login.jsp";
	}
	
	/**
	 * 用户登录的执行的方法:login
	 */
	public String login(HttpServletRequest req,HttpServletResponse resp){
		try{
			// 一次性验证码程序:
			String code1 = req.getParameter("code");
			String code2 = (String)req.getSession().getAttribute("code");
			req.getSession().removeAttribute("code");
			if(!code1.equalsIgnoreCase(code2)){
				req.setAttribute("msg", "验证码输入错误!");
				return "/jsp/login.jsp";
			}
			
			// 接收参数:
			Map<String,String[]> map = req.getParameterMap();
			// 封装数据：
			User user = new User();
			BeanUtils.populate(user, map);
			// 调用业务层:
			UserService userService = (UserService) BeanFactory.getBean("userService");
			User existUser = userService.login(user);
			if(existUser == null){
				req.setAttribute("msg", "用户名或密码或用户未激活!");
				return "/jsp/login.jsp";
			}else{
				// 登录成功:自动登录
				String autoLogin = req.getParameter("autoLogin");
				if("true".equals(autoLogin)){
					Cookie cookie = new Cookie("autoLogin", existUser.getUsername()+"#"+existUser.getPassword());
					cookie.setPath("/store_v2.0");
					cookie.setMaxAge(7* 24 * 60 * 60);
					resp.addCookie(cookie);
				}
				
				// 记住用户名:
				String remember = req.getParameter("remember");
				if("true".equals(remember)){
					Cookie cookie = new Cookie("username",existUser.getUsername());
					cookie.setPath("/store_v2.0");
					cookie.setMaxAge(24 * 60 * 60);
					resp.addCookie(cookie);
				}
				
				req.getSession().setAttribute("existUser", existUser);
				resp.sendRedirect(req.getContextPath()+"/index.jsp");
				return null;
			}
			// 页面跳转
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 用户退出功能的方法:logOut
	 * @param req
	 * @param resp
	 * @return
	 */
	public String logOut(HttpServletRequest req,HttpServletResponse resp){
		
		try {
			// 销毁session
			req.getSession().invalidate();
			// 清空自动登录的Cookie:
			Cookie cookie = new Cookie("autoLogin","");
			cookie.setPath("/store_v2.0");
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
			
			resp.sendRedirect(req.getContextPath()+"/index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
