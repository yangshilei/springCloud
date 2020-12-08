package com.demo.mycat.mapper;

import com.demo.mycat.dto.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TenantMapper {
    List<Tenant> queryAllTenant();
}
