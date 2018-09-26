package com.jaremo.test_shiro.ssshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class LoginController {

    @RequestMapping("/loginshiro")
    public String login(String userName, String password, Map map){
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                currentUser.login(token);
                map.put("msg","登录状态: "+(currentUser.isAuthenticated()?"已登录":"未登录"));
                return "/shiropage/suc.jsp";
            } catch (UnknownAccountException uae) { // 账号错误
                map.put("msg","账号错误");
            } catch (IncorrectCredentialsException ice) { // 密码错误
                map.put("msg","密码错误");
            } catch (LockedAccountException lae) { // 账户锁定
                map.put("msg","账户锁定");
            } catch (AuthenticationException ae) { // 未知错误
                map.put("msg","未知错误");
            }
        }
        return "/shiropage/fail.jsp";
    }
}
