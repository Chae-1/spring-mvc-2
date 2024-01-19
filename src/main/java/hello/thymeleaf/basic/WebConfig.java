package hello.thymeleaf.basic;

import hello.thymeleaf.basic.filterAndInterceptor.LogFilter;
import hello.thymeleaf.basic.filterAndInterceptor.LogInterceptor;
import hello.thymeleaf.basic.filterAndInterceptor.LoginFilter;
import hello.thymeleaf.basic.filterAndInterceptor.LoginMemberArgumentResolver;
import jakarta.servlet.Filter;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
        registry.addInterceptor(new LogInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/login", "/logout", "/members/add", "/");


    }

    //    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogFilter());
        bean.setOrder(1);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean2() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoginFilter());
        bean.setOrder(2);
        bean.addUrlPatterns("/*");
        return bean;
    }


}
