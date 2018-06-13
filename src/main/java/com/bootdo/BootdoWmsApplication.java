package com.bootdo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * <p>war包发布方式<p/>
 * @ProjectName:
 * @ClassName: BootdoWmsApplication
 * @Description: 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 * @Author: sky.liu
 * @Date: 2018/6/13 22:42
 * @Version 1.0
 * @ChangeLog
 */
public class BootdoWmsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(BootdoApplication.class);
    }
}
