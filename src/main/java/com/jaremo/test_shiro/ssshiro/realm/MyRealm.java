package com.jaremo.test_shiro.ssshiro.realm;

import com.jaremo.test_shiro.ssshiro.dao.UserMapper;
import com.jaremo.test_shiro.ssshiro.domain.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /*
        授权
            多表查询出该用户的角色以及权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = principalCollection.getPrimaryPrincipal().toString();
        Set<String> roles = userMapper.queryRoleByUserName(userName);
        Set<String> perms = userMapper.queryPermByUserName(userName);
        SimpleAccount sa = new SimpleAccount();
        sa.setRoles(roles);
        sa.setStringPermissions(perms);
        return sa;
    }

    /*
        认证
            controller层传入参数 与数据库的中的数据进行比较
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        User tempUser = userMapper.queryUserByUserName(token.getUsername());
        System.out.println(tempUser);
        if(tempUser!=null && tempUser.getPassword().equals(new String(token.getPassword()))){
            SimpleAccount sa = new SimpleAccount(token.getUsername(),token.getPassword(),"MyRealm");
            return sa;
        }
        return null;
    }
}
