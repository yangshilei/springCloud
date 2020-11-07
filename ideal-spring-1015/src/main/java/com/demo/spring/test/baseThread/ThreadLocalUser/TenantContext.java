package com.demo.spring.test.baseThread.ThreadLocalUser;

/** 
 * @Description: 创建上下文对象 ，用于传递参数，从前端传递到控制层等；
 * @Author: yangshilei
 * @Date:
 */
public class TenantContext {

    // 初始化线程中对象，防止空指针异常，方便设置对象属性值；
    // 因为它默认是返回的空；
    // 不能用来存储大的对象
    // 使用private static final进行修饰，防止多实例时内存泄漏的问题
    // 分布式环境不能用ThreadLocal直接传递参数，因为不是同一个JVM；
    private static final ThreadLocal<Tenant> threadLocal = new ThreadLocal<Tenant>(){
        @Override
        protected Tenant initialValue() {
            return new Tenant();
        }
    };

    // 获取线程中租户
    public static Tenant getTenant(){
        return threadLocal.get();
    }

    // 移除线程中的租户
    public static void removeValue(Object key){
        Tenant tenant = threadLocal.get();
        if(null != tenant){
            threadLocal.remove();
        }
    }

}
