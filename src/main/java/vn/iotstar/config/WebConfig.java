package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.iotstar.interceptor.AuthInterceptor;


//Lớp này được sử dụng để tùy chỉnh và mở rộng các hành vi mặc định của ứng dụng web
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    
    //Phương thức này chỉ định các URL pattern mà interceptor sẽ áp dụng. Ở đây, interceptor chỉ hoạt động trên các đường dẫn bắt đầu bằng /admin/
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/admin/**");
    }
}