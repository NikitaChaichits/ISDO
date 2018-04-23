package by.i4t.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Ilya on 04.11.2016.
 */
@Configuration
@EnableJpaRepositories("by.i4t.repository")
@ComponentScan({"by.i4t.repository", "by.i4t.service"})
@EnableTransactionManagement
public class TestConfig {
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource driverDataSource = new SimpleDriverDataSource();
        driverDataSource.setDriverClass(org.postgresql.Driver.class);
        driverDataSource.setUrl("jdbc:postgresql://vm-pgsl2.lab.circ.by:5432/edu_org_new");
        driverDataSource.setUsername("postgres");
        driverDataSource.setPassword("circus16");
        return driverDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("by.i4t.objects");
        factory.setDataSource(dataSource());
        Properties props = new Properties();
        props.put("hibernate.event.merge.entity_copy_observer", "allow");
        factory.setJpaProperties(props);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}
