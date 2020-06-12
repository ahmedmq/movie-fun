package org.superbiz.moviefun.albums;

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
public class AlbumConfig {

    @Autowired
    private HibernateJpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private DatabaseServiceCredentials serviceCredentials;

    @Bean
    public DataSource albumsDataSource() {
        HikariDataSource hks = new HikariDataSource();
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql"));
        hks.setDataSource(dataSource);
        return hks;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumEntityManager(){

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(albumsDataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(new String[] {"org.superbiz.moviefun.albums"});
        entityManagerFactoryBean.setPersistenceUnitName("Albums");

        return entityManagerFactoryBean;

    }

    @Bean
    public PlatformTransactionManager albumTransactionManager(){

        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(albumEntityManager().getObject());
        return manager;
    }

}
