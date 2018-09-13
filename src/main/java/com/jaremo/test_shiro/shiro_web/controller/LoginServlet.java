package com.jaremo.test_shiro.shiro_web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        doGet(request,response)  ;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        String username = request.getParameter("userName");
        String password = request.getParameter("password");

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username , password);

        try{
            currentUser.login(token);
            request.getRequestDispatcher("/shiropage/suc.jsp").forward(request,response);
        }catch (AuthenticationException e){
            request.getRequestDispatcher("/shiropage/fail.html").forward(request,response);
        }

    }
}
