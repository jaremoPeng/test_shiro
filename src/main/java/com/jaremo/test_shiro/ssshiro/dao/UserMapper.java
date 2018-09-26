package com.jaremo.test_shiro.ssshiro.dao;

import com.jaremo.test_shiro.ssshiro.domain.Menu;
import com.jaremo.test_shiro.ssshiro.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserMapper {

    @Select("select * from users where userName=#{userName}")
    public User queryUserByUserName(@Param("userName") String userName);

    @Select("select r.roleName from users u" +
            " INNER join users_roles_relation urr on u.userid = urr.userid" +
            " inner join roles r on urr.roleid=r.roleid" +
            " where userName=#{userName}")
    public Set<String> queryRoleByUserName(@Param("userName") String userName);

    @Select("select p.permTag from users u" +
            " INNER join users_roles_relation urr on u.userid = urr.userid" +
            " inner join roles r on urr.roleid=r.roleid" +
            " inner join roles_perms_relation rpr on r.roleid = rpr.roleid" +
            " inner join perms p on rpr.permid=p.permid" +
            " where userName=#{userName}")
    public Set<String> queryPermByUserName(@Param("userName") String userName);

    @Select("select * from menu")
    public List<Menu> queryMenu();

    @Select("select * from menu where menuUrl=#{menuUrl}")
    public Menu queryMenuByMenuUrl(@Param("menuUrl") String menuUrl);
}
