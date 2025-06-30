package com.tgmeng.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description: 跨域配置类
 * package: com.tgmeng.common.config
 * className: CustomCORSConfiguration
 *
 * @author tgmeng
 * @version v1.0
 * @since 2025/6/30 21:30
*/
@Configuration
public class CustomCORSConfiguration implements WebMvcConfigurer {
    /**
     * description: 跨域放行
     * package: com.tgmeng.common.config
     * className: CustomCORSConfiguration
     *
     * @author tgmeng
     * @version v1.0
     * @since 2025/6/30 21:31
    */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有的当前站点的请求地址，都支持跨域访问。
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
                .allowedOriginPatterns("*")
                //当前站点支持的跨域请求类型是什么
                .allowedMethods("*")
                //	允许请求头中的header，默认都支持
                .allowedHeaders("*")
                .allowCredentials(true)
                //响应头中允许访问的header，默认为空
                .exposedHeaders("*")
                //预请求的结果的有效期，默认30分钟,这里为一天
                .maxAge(24 * 60 * 60);
    }
}
