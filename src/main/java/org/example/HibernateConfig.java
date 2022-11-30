package org.example;

import org.example.managers.IDbWorkerManager;
import org.example.properties.ApplicationProperties;
import org.example.properties.DbProperties;
import org.example.properties.JpaHibernateProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Autowired
    @Qualifier("db_manager")
    IDbWorkerManager dbWorkerManager;

    @Autowired
    ApplicationProperties config;

    @Bean(name = "lcemf")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dbWorkerManager.getDataSourceFromConnection(config.getDbProperties().getConnections().get(DbProperties.SQL_SERVER_WINDOWS)));
        em.setPackagesToScan(new String[] { "org.example.data.model" }); //package to Entities.

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties()); //settings from JpaHibernateProperties (config file application.yml section 'jpa:hibernate:')

        em.afterPropertiesSet();
        return em;
    }


    @Bean(name = "tm")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        transactionManager.setDataSource(entityManagerFactory().getDataSource());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

/*
    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }
*/

    Properties additionalProperties() {
        Properties properties = new Properties();
        JpaHibernateProperties.HibernateProperties config_props = config.getJpaProperties().getProps().get(JpaHibernateProperties.JPA_EMPTY_DB);
        JpaHibernateProperties enhanced_props = config.getJpaProperties();


        //set session_context_class
        //org.springframework.orm.hibernate5.SpringSessionContext
        //org.hibernate.context.internal.ThreadLocalSessionContext
        properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");


        properties.setProperty("hibernate.hbm2ddl.auto", config_props.getDdlAuto());
        properties.setProperty("hibernate.show-sql", ""+config_props.isShowSql());
        properties.setProperty("hibernate.dialect", config_props.getDialect());

        if(config_props.getImplicitNameStrategy() != null && config_props.getImplicitNameStrategy().length() > 0)
            properties.setProperty("hibernate.naming.implicit-strategy", config_props.getImplicitNameStrategy());
        if(config_props.getPhysicalNameStrategy() != null && config_props.getPhysicalNameStrategy().length() > 0)
            properties.setProperty("hibernate.naming.physical-strategy", config_props.getPhysicalNameStrategy());
        if(config_props.getImportFileName() != null && config_props.getImportFileName().length() > 0)
            properties.setProperty("hibernate.hbm2ddl.import_files", config_props.getImportFileName());

        properties.setProperty("hibernate.jdbc.time_zone", "UTC");

        //legacy props for Hibernate prior to version 5.
        properties.setProperty("hibernate.id.disable_delayed_identity_inserts", "true");
        properties.setProperty("hibernate.id.new_generator_mappings", "true"); //by default is false if Hibernate version < 5.


        //enhanced properties (if not specified => false)
        //REQUIRES JAVA-AGENT FOR BYTECODE MODIFICATIONS!!!
        properties.setProperty("hibernate.enhancer.enableDirtyTracking", enhanced_props.isDirtyTracking()+"");
        properties.setProperty("hibernate.enhancer.enableLazyInitialization", enhanced_props.isLazyInitialization()+"");
        properties.setProperty("hibernate.enhancer.enableAssociationManagement", enhanced_props.isAssociationManagement()+"");

        //common-mapping properties
        //map any text into National (e.g. default NVARCHAR, NTEXT, NCLOB instead of VARCHAR, TEXT ...)
        properties.setProperty("hibernate.use_nationalized_character_data", "true");

        //quote keywords by default (aka 'user_id', 'number', 'timestamp' 'join' and so on)
        properties.setProperty("hibernate.auto_quote_keyword", "true");

        //map boolean type to BIT
        properties.setProperty("hibernate.type.preferred_boolean_jdbc_type", "-7"); //java.sql.Types.BIT value
        properties.setProperty("hibernate.type.preferred_uuid_jdbc_type", "3000"); //store uuid as UUID

        System.out.println(properties);

        return properties;
    }
}
