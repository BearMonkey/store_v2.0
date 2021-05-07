package com.itheima.store.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.utils.BaseServlet;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.Result;
import com.itheima.store.utils.StringUtils;

public class AdminServlet extends BaseServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -7667378683404854320L;

    /**
     * 
     * @param req
     * @param resp
     * @return
     */
    public String adminLogin(HttpServletRequest req,HttpServletResponse resp) {
        System.out.println("adminLogin");
        String username = req.getParameter("username");
        String passwd = req.getParameter("password");
        if (StringUtils.isEmpty(username)) {
            System.out.println("用户名不能为空");
            req.setAttribute("errorUsername", "用户名不能为空");
            return "/admin/index.jsp";
        }
        if (StringUtils.isEmpty(passwd)) {
            System.out.println("密码不能为空");
            req.setAttribute("errorPasswd", "密码不能为空");
            return "/admin/index.jsp";
        }
        UserService userService = (UserService) BeanFactory.getBean("userService");
        User existUser;
        try {
            existUser = userService.findByUsername(username);
            if(existUser == null){
                System.out.println("用户不存在");
                // 用户名不存在:
                req.setAttribute("errorPasswd", "用户不存在");
                return "/admin/index.jsp";
            }
            System.out.println("login success");

            req.getSession().setAttribute("adminUsername", existUser.getUsername());
            req.getSession().setAttribute("adminUserid", existUser.getUid());
            resp.sendRedirect(req.getContextPath()+"/admin/home.jsp");
            return null;
        } catch (SQLException | IOException e) {
            System.out.println("登录时发生未知错误");
            req.setAttribute("error", "登录时发生未知错误");
            return "/admin/error.jsp";
        }
    }

}
