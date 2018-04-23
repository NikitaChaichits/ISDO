package edu.org.spring.config;

import by.i4t.log.EduDocsDBAppender;
import edu.org.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"edu.org.service", "edu.org.validators"})
@EnableJpaRepositories("by.i4t.repository")
@EnableTransactionManagement
@EnableScheduling
@Import({ServiceConfig.class, CtrlConfig.class, DBConfig.class})
@PropertySource(name = "app.props", value = "${app.prop.path}")
public class AppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private RepositoryService repositoryService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/pages");
        resolver.setSuffix(".xhtml");
        return resolver;
    }

    @Bean(name = "dbAppender")
    public EduDocsDBAppender dbAppender() {
        return EduDocsDBAppender.createAppender(
                "EduDocsDBAppender", null, null, null,
                repositoryService.getEduDocsAppSettingsRepository(),
                repositoryService.getLogsRepository());
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

}
