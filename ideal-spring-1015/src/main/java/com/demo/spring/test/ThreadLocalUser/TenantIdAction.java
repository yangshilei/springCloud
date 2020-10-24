package com.demo.spring.test.ThreadLocalUser;



/**
 * @Description: 为上下文中取出的租户设置tenantId和tenantName
 * @Author: yangshilei
 * @Date:
 */
public class TenantIdAction {

    // 从上下问获取租户对象，并给租户对象设置id值；id为当前线程的id；
    public void setTenantId(){
        Tenant tenant = TenantContext.getTenant();
        tenant.setTenantId(this.getTenantId());
    }

    // 用"租户-线程id"来拼接租户名称
    public void setTenantName(){
        String name = "杨" + TenantContext.getTenant().getTenantId();
        TenantContext.getTenant().setTenantName(name);
    }



    // 获取线程id作为租户id
    public String getTenantId(){
        return String.valueOf(Thread.currentThread().getId());
    }


}

