package com.rainett.config;

import com.rainett.init.StorageInitializer;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(value = "com.rainett.repository")
public class JpaConfig {
    @Value("${data.driverClassName}")
    private String driverClassName;

    @Value("${data.url}")
    private String url;

    @Value("${data.username}")
    private String username;

    @Value("${data.password}")
    private String password;

    @Value("${data.showSql}")
    private String showSql;

    @Value("${data.file}")
    private String dataFile;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean emfBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emf.setPackagesToScan("com.rainett.model");
        emf.setJpaProperties(jpaProperties());
        return emf;
    }

    private Properties jpaProperties() {
        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.show_sql", showSql);
        return props;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean(initMethod = "initializeData")
    public StorageInitializer storageInitializer(DataSource dataSource) {
        return new StorageInitializer(dataFile, dataSource);
    }
}
