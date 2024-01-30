package org.example.hacking02_sk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    // 모든 Bean을 관리하는 ApplicationContext 객체 불러오기(새롭게 생성 X)
    @Autowired
    ApplicationContext applicationContext;

    // JSP ViewResolver
    @Bean // Bean은 일종의 클래스들 Spring은 모든 클래스를 Bean으로 만들어서 ApplicationContext가 관리
    public ViewResolver jspViewResolver() {
        // 새로운 ViewResolver 생성
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        // ViewResolver Prefix, Suffix, ViewNames 설정
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewNames("jsp/*");
        return resolver;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        // Spring Framework가 인식할 수 있게 SpringResourceTemplateResolver 생성
        // Thymeleaf Resolver를 생성해서 인코딩, 경로를 설정하기 위해 생성
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:/templates/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(true); // Resolver에 설정한대로 존재하는지 확인
        return resolver;
    }

    // Thymeleaf 엔진을 이용하기 위해 SpringTemplateEngine을 생성
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        // 위에서 생성한 Template Resolver를 SpringTemplateEngine에 설정해줌
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    // 실제 Thymeleaf View Resolver 생성
    // 인코딩부터 Controller가 어떤 값을 Return할 때 작동할지 설정
    @Bean
    public ViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("utf-8");
        resolver.setOrder(1);
        resolver.setViewNames(new String[] {"*.html", "*"});
        resolver.setCache(false);
        resolver.setExcludedViewNames(new String[] {"jsp/*"});
        return resolver;
    }

    // 위에서 Jsp, Thymeleaf View Resolver가 작동할 수 있게 Registry에 저장
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(jspViewResolver());
        registry.viewResolver(thymeleafViewResolver());
    }

    // 이미지, 파일 등 정적 파일의 경로를 알 수 있게 static 폴더 Registry에 저장
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
