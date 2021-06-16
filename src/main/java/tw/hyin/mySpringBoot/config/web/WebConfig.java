package tw.hyin.mySpringBoot.config.web;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author H-yin on 2020.
 *         介接通道許可設定
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //註冊攔截器
        registry.addInterceptor(new UserInterceptor());
        Field registrationsField = FieldUtils.getField(InterceptorRegistry.class, "registrations", true);
        List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) ReflectionUtils.getField(registrationsField, registry);
        if (registrations != null) {
            for (InterceptorRegistration interceptorRegistration : registrations) {
                interceptorRegistration
                        //排除swagger攔截
                        .excludePathPatterns("/swagger**/**")
                        .excludePathPatterns("/webjars/**")
                        .excludePathPatterns("/v3/**")
                        .excludePathPatterns("/swagger-resources/**");
            }
        }
    }

}
