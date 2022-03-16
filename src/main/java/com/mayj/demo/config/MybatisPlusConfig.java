package com.mayj.demo.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MybatisPlusConfig
 * @Description mybatis-plus配置类
 * @Author Mayj
 * @Date 2022/3/12 14:49
 **/
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     * 需要注意的是Page参数需要放在参数列表的第一个
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     *  mybatis配置
     * 此处用bean的形式注入，数据库配置时就不用重复配置,但需要设置注入顺序
     *  经过使用sqlite测试，不可注入相同的配置，会出错！具体原因不详,因为注入方式为单例？
     */
    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.addInterceptor(new PaginationInterceptor());
        return configuration;
    }
}
