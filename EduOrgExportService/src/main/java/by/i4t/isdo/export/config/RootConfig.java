package by.i4t.isdo.export.config;

import by.i4t.isdo.export.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by roman on 28.11.2016.
 */
@Configuration
@ComponentScan(basePackages = {"by.i4t.isdo.export"})
@EnableTransactionManagement
@Import({InfrastructureConfig.class})
@PropertySource(name = "app.props", value = "classpath:app.properties")
public class RootConfig {
    @Autowired
    MainService mainService;

}
