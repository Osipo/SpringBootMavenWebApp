package org.example;

import org.example.properties.ApplicationProperties;
import org.example.properties.DbProperties;
import org.example.utils.MediaTypeMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;


@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class
        }
)
@ConfigurationPropertiesScan(basePackages = {"properties"}) //depends on current package of class.

//THESE ANNOTATIONS REQUIRED FULLY-QUALIFIED PACKAGE NAMES STARTING FROM 'java' directory!!!
@EnableJpaRepositories(
        basePackages =  {"org.example.data.repository"},
        entityManagerFactoryRef = "lcemf",
        transactionManagerRef = "tm",
        enableDefaultTransactions = false
)

//@EnableTransactionManagement(proxyTargetClass = true)
//@EntityScan("org.example.data.model") //manually set at HibernateConfig.entityManagerFactory()
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Show beans");

            /*
                show all beans (include from framework)

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
             */

            //SHOW MEDIATYPES MAP
            Object b = ctx.getBean("str_media_map_with_aliases");
            ApplicationProperties config = ctx.getBean(ApplicationProperties.class);

            if(config.getHttpProperties().getError() != null){
                for(Map.Entry<String, Object> r: config.getHttpProperties().getError().entrySet())
                    System.out.println(r.getKey() +": " + r.getValue());
            }

            if(config.getDbProperties() == null || config.getDbProperties().getConnections() == null){
                System.err.println("Cannot load db configuration. Startup failed.");
                System.exit(-1);
            }

            for(DbProperties.DbConnection c: config.getDbProperties().getConnections()){
                System.out.println("connection " + c.toString());
            }

            if(b != null && b instanceof MediaTypeMap) {
                System.out.println("Map of names of mediaTypes created.");
                MediaTypeMap map = (MediaTypeMap) b;
                System.out.println(b);
            }
        };
    }
}