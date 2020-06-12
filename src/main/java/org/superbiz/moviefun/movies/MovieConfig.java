package org.superbiz.moviefun.movies;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.superbiz.moviefun.DatabaseServiceCredentials;

import javax.sql.DataSource;

@Configuration
public class MovieConfig {

    @Autowired
    private HibernateJpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private DatabaseServiceCredentials serviceCredentials;

    @Bean
    public DataSource moviesDataSource() {
        HikariDataSource hks = new HikariDataSource();
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        hks.setDataSource(dataSource);
        return hks;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManager(){

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(moviesDataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(new String[] {"org.superbiz.moviefun.movies"});
        entityManagerFactoryBean.setPersistenceUnitName("Movies");

        return entityManagerFactoryBean;

    }

    @Bean
    public PlatformTransactionManager movieTransactionManager(){

        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(moviesEntityManager().getObject());
        return manager;
    }
}
