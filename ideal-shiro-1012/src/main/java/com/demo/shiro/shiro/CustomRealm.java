package com.demo.shiro.shiro;

import com.demo.shiro.dto.Permissions;
import com.demo.shiro.dto.Role;
import com.demo.shiro.dto.User;
import com.demo.shiro.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    /**
     * 登陆认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 加这步的目的是在post请求时候会先进行认证，然后再到请求
        if(null == authenticationToken.getPrincipal()){
            return null;
        }
        // 获取用户信息：底层源码，principal接口下存的就算username
        String username = authenticationToken.getPrincipal().toString();

        User user = loginService.getUserByName(username);
        if(null == user){
            // 这里返回后会报出对应的异常
            return null;
        }else{
            /**
             * 参数说明：
             * 1.用户名–此处传的是用户对象
             * 2.密码—从数据库中获取的密码
             * 3.盐–用于加密密码对比，–获取的经验：为了防止两用户的初始密码是一样的 （选传）
             * 4.当前的realm名:getName()
             */
            SimpleAuthenticationInfo simpleAuthenticationInfo =
                    new SimpleAuthenticationInfo(username,user.getPassword().toString(),getName());
            return simpleAuthenticationInfo;
        }
    }

    /**
     * 鉴权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登陆的用户名
        String name = (String)principalCollection.getPrimaryPrincipal();
        // 根据用户名去数据库查询用户信息
        User user = loginService.getUserByName(name);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 如果用户有角色和权限，则添加到认证信息中
        if(null != user.getRoles() && !user.getRoles().isEmpty()){
            for(Role role : user.getRoles()){
                simpleAuthorizationInfo.addRole(role.getRoleName());
                for(Permissions permissions : role.getPermissions()){
                    simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
                }
            }
        }


        return simpleAuthorizationInfo;
    }



}
