package com.jaremo.test_shiro.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.jaremo.test_shiro.ssshiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
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

    // 构建简单的用户
    SimpleAccountRealm sar = new SimpleAccountRealm();
    @Before
    public void addUser(){
       sar.addAccount("admin","123456","role1","role2");
    }
    @Test
    public void test2() {
        // 构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(sar);

        // 构建主体对象
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token); // 认证用户

        // 是否认证
        System.out.println("isAuthenticated: "+subject.isAuthenticated());

        subject.checkRoles("role1","role2"); // 授权
    }

    @Test
    public void test3() {
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini"); // 引用外部的ini文件(读取ini文件中的内容)
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); // 主体对象就是已经被认证的当前用户

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());
        subject.checkRoles("role1"); // 检查主体对象有些角色
        subject.checkPermissions("user:query","user:delete"); //  检查主体对象有些权限
    }

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql//localhost:8080/account");
        dataSource.setDriverClassName("");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void test4() {
        JdbcRealm jdbcRealm = new JdbcRealm(); // 数据库
        jdbcRealm.setDataSource(dataSource);
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); // 主体对象就是已经被认证的当前用户

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());
    }

    @Test
    public void test5() {

        CustomRealm customRealm=new CustomRealm();
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm); // 设置自定义realm

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); // 主体对象

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());

        subject.checkRoles("role1");
        subject.checkPermissions("user:select");
    }

    @Test
    public void test6() {

        CustomRealm customRealm=new CustomRealm();
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm); // 设置自定义realm

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(); // 加密的工具类
        matcher.setHashAlgorithmName("md5"); // 设置加密算法的名字 md5
        matcher.setHashIterations(1); // 设置加密次数
        customRealm.setCredentialsMatcher(matcher); // 给自定义realm添加加密算法

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); // 主体对象

        UsernamePasswordToken token = new UsernamePasswordToken("admin","123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());
    }
}
