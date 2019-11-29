package com.drofff.edu.config;

import com.drofff.edu.component.OnlineInterceptor;
import com.drofff.edu.component.ProfileDataInterceptor;
import com.drofff.edu.component.UserContext;
import com.drofff.edu.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserContext userContext;

    @Autowired
    private FilesService filesService;

    @Autowired
    private OnlineInterceptor onlineInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ProfileDataInterceptor(userContext, filesService));
        registry.addInterceptor(onlineInterceptor);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/");
    }

}
