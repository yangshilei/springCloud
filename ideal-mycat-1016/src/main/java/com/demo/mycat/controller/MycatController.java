package com.demo.mycat.controller;

import com.demo.mycat.dto.Result;
import com.demo.mycat.dto.Tenant;
import com.demo.mycat.mapper.TenantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mycat")
@Slf4j
public class MycatController {

    @Autowired
    private TenantMapper tenantMapper;

    @GetMapping
    Result getTest(){
        List<Tenant> tenants = tenantMapper.queryAllTenant();
        if(null != tenants && !tenants.isEmpty()){
            return Result.ok(tenants);
        }
        return Result.ok("未查询到数据！");
    }

}
