package com.itheima.store.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.service.impl.UserServiceImpl;
import com.itheima.store.utils.CookieUtils;

/**
 * 自动登录的过滤器的实现
 * @author admin
 *
 */
public class AutoLoginFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/**
		 * 判断session中是否有用户的信息:
		 * * session中如果有:放行.
		 * * session中没有:
		 *    * 从Cookie中获取:
		 *        * Cookie中没有:放行.
		 *        * Cookie中有:
		 *            * 获取Cookie中存的用户名和密码到数据库查询.
		 *                * 没有查询到:放行.
		 *                * 查询到:将用户信息存入到session . 放行.
		 */
		// 判断session中是否有用户的信息:
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		User existUser = (User) session.getAttribute("existUser");
		if(existUser != null){
			// session中有用户信息.
			chain.doFilter(req, response);
		}else{
			// session中没有用户信息.
			// 获得Cookie的数据:
			Cookie[] cookies = req.getCookies();
			Cookie cookie = CookieUtils.findCookie(cookies, "autoLogin");
			// 判断Cookie中有没有信息:
			if(cookie == null){
				// 没有携带Cookie的信息过来:
				chain.doFilter(req, response);
			}else{
				// 带着Cookie信息过来.
				String value = cookie.getValue();// aaa#111
				// 获得用户名和密码:
				String username = value.split("#")[0];
				String password = value.split("#")[1];
				// 去数据库进行查询:
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				UserService userService = new UserServiceImpl();
				try {
					existUser = userService.login(user);
					if(existUser == null){
						// 用户名或密码错误:Cookie被篡改的.
						chain.doFilter(req, response);
					}else{
						// 将用户存到session中,放行
						session.setAttribute("existUser", existUser);
						chain.doFilter(req, response);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void destroy() {
		
	}

}
