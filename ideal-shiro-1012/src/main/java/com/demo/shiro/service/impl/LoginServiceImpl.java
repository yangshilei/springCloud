package com.demo.shiro.service.impl;

import com.demo.shiro.dto.Permissions;
import com.demo.shiro.dto.Role;
import com.demo.shiro.dto.User;
import com.demo.shiro.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public User getUserByName(String getMapByName) {
        log.info("进入根据用户名查询用户认证和权限信息方法");
        //模拟数据库查询，正常情况此处是从数据库或者缓存查询。
        return this.getMapByName(getMapByName);
    }

    /**
     * 查询用户以及用户角色和对应角色下的权限情况
     * @param userName
     * @return
     */
    private User getMapByName(String userName){
        //模拟数据库中的两个用户，两个用户都是admin一个角色，
        //wsl有query和add权限，zhangsan只有一个query权限
        Permissions permissions1 = new Permissions("1","query");
        Permissions permissions2 = new Permissions("2","add");
        Set<Permissions> permissionsSet = new HashSet<>();
        permissionsSet.add(permissions1);
        permissionsSet.add(permissions2);

        Role role = new Role("1","admin",permissionsSet);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user = new User("1","wsl","123456",roleSet);
        Map<String ,User> map = new HashMap<>();
        map.put(user.getUserName(), user);

        Permissions permissions3 = new Permissions("3","query");
        Set<Permissions> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(permissions3);

        Role role1 = new Role("2","user",permissionsSet1);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);

        User user1 = new User("2","zhangsan","123456",roleSet1);
        map.put(user1.getUserName(), user1);

        return map.get(userName);
    }
}
