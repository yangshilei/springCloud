package com.demo.shiro.controller;

import com.demo.shiro.dto.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("shiro认证鉴权管理")
@RestController
public class LoginController {

    @ApiOperation(value = "登陆接口", notes = "登陆接口")
    @GetMapping("/login")
    public String login( User user){
        // 添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(),user.getPassword());
        try {
            subject.login(usernamePasswordToken);
            subject.checkRole("admin");
            subject.checkPermissions("query", "add");
        }catch (AuthenticationException e){
            e.printStackTrace();;
            return "账号或密码错误";
        }catch (AuthorizationException e){
            e.printStackTrace();
            return "没有权限";
        }
        return "login success!";
    }

    //注解验角色和权限:只能验证角色和权限无法捕捉异常
    @RequiresRoles("admin")
    @RequiresPermissions("add")
    @RequestMapping("/index")
    public String index(){
        return "index!";
    }

}
