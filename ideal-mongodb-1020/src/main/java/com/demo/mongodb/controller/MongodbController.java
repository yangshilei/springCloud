package com.demo.mongodb.controller;

import com.demo.mongodb.dto.Result;
import com.demo.mongodb.dto.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "mongoDB模块")
@RestController
@Slf4j
@RequestMapping("/mongodb")
public class MongodbController {

    @Autowired
    private MongoTemplate template;

    @ApiOperation(value = "新增单个用户", notes = "新增单个用户")
    @PostMapping(value = "/insert")
    public Result insertObj(@RequestBody  User user){
        log.info("新增用户入参=={}",user.toString());
        user.setCreateDate(new Date());
        User save = template.save(user);
        log.info("插入数据==={}",save.toString());
        return Result.ok("新增成功");
    }

    @ApiOperation(value = "批量新增用户", notes = "批量新增用户")
    @PostMapping(value = "/batchInsert")
    public Result batchInsert(@RequestBody List<User> users){
        log.info("批量新增用户入参=={}",users.toString());
        for(User item : users){
            template.save(item);
        }
        return Result.ok("批量新增成功");
    }

    @ApiOperation(value = "根据id查询", notes = "批量新增用户")
    @PostMapping(value = "/getById")
    public Result getById(@RequestBody  User user){
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        User one = template.findOne(query, User.class);
        return Result.ok(one);
    }

    @ApiOperation(value = "根据用户名称查询", notes = "根据用户名称查询")
    @PostMapping(value = "/getByUsername")
    public Result getByUsername(@RequestBody  User user){
        Query query = new Query(Criteria.where("username").is(user.getUsername()));
        User one = template.findOne(query, User.class);
        return Result.ok(one);
    }

    @ApiOperation(value = "更新用户hobby和company", notes = "更新用户hobby和company")
    @PutMapping(value = "/updateUser")
    public Result updateUser(@RequestBody  User user){
        log.info("更新用户入参==={}",user.toString());
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update  = new Update();
        update.set("hobby",user.getHobby());
        update.set("company",user.getCompany());
        UpdateResult updateResult = template.updateFirst(query, update, User.class);
        log.info("更新的结果==={}",updateResult.toString());
        return Result.ok("更新成功！");
    }

    @ApiOperation(value = "根据id删除用户", notes = "根据id删除用户")
    @DeleteMapping(value = "/deleteByID")
    public Result deleteByID(@RequestBody  User user){
        log.info("根据id删除用户请求==={}",user.toString());
        DeleteResult remove = template.remove(user);
        log.info("删除的结果==={}",remove);
        return Result.ok("删除成功");
    }


}
