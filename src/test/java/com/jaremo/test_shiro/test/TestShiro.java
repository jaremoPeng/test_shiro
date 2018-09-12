package com.jaremo.test_shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class TestShiro {

    @Test
    public void test1() {
        // 读取ini文件 构建SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = (SecurityManager) factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 获取当前用户
        Subject currentUser = SecurityUtils.getSubject(); // 主体对象,指谁调用,谁就是这个对象(无论是程序,还是用户...)

        // 获取当前用户的会话
        Session session = currentUser.getSession();

        if (!currentUser.isAuthenticated()) { // 判断当前用户是否已经登录 默认是false(isAuthenticated())
            // 这里传递的两个参数跟配置文件ini有关
            UsernamePasswordToken token = new UsernamePasswordToken("username", "password");

            try {
                currentUser.login(token);
                System.out.println("登录成功");
                System.out.println("登录状态: "+(currentUser.isAuthenticated()?"已登录":"未登录"));
                if(currentUser.hasRole("role1")){ // 判断当前这个用户是否拥有指定的角色
                    System.out.println("当前这个用户拥有role1这个角色");
                }
                if(currentUser.isPermitted("user:query:1")){ // 判断当前这个用户是否拥有指定的指定的权限
                    System.out.println("当前这个用户拥有这个权限");
                }
            } catch (UnknownAccountException uae) { // 账号错误
                System.out.println("账号错误");
            } catch (IncorrectCredentialsException ice) { // 密码错误
                System.out.println("密码错误");
            } catch (LockedAccountException lae) { // 账户锁定
                System.out.println("账户锁定");
            } catch (AuthenticationException ae) { // 未知错误
                System.out.println("未知错误");
            }
        }

    }
}
