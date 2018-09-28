package com.jaremo.test_shiro.ssshiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {
    Map<String,String> userMap = new HashMap<>();

    {
        userMap.put("admin",getMatcher("123456")); // 初始化数据
        super.setName("customRealm"); // 设置自定义realm临时名字
    }

    public String getMatcher(String encryptStr){
//        Md5Hash md5Hash = new Md5Hash(encryptStr); // 不加盐 , 不能解密
        Md5Hash md5Hash = new Md5Hash(encryptStr,"salt");// 加盐的加密
        return  md5Hash.toString();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    // 相当于 从数据库中或者缓存中获取数据
    public Set<String> getPermissionsByUsername(String username){
        Set<String> perms = new HashSet<>();
        perms.add("user:delete");
        perms.add("user:select");
        return perms;
    }

    // 相当于 从数据库中或者缓存中获取数据
    public Set<String> getRolesByUsername(String username){
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        roles.add("role2");
        return roles;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal(); // 接收的是用户传入的登录名
        String password = getPasswordByUsername(username);
        if(password != null){
            SimpleAuthenticationInfo simpleAuthenticationInfo =
                    new SimpleAuthenticationInfo("admin",password,"customRealm"); // 设置简单的认证信息
            simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("salt")); // 使用加盐的加密的话, 在此设置加盐的字符串
            return simpleAuthenticationInfo; // 主体对象是以这个为标准的
        }
        return null;
    }

    public String getPasswordByUsername(String username){
        if(username != null){
            return userMap.get(username);
        }
        return null;
    }
}
