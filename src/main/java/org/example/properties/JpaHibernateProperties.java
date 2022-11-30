package org.example.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "jpa.hibernate", ignoreInvalidFields = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("hibernate_props")
public class JpaHibernateProperties {

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class HibernateProperties {

        @ConstructorBinding
        public HibernateProperties(String ddlAuto, String dialect, boolean showSql, String implicitNameStrategy, String physicalNameStrategy, String importFileName){
            this.ddlAuto = ddlAuto;
            this.dialect = dialect;
            this.showSql = showSql;

            this.implicitNameStrategy = implicitNameStrategy;
            this.physicalNameStrategy = physicalNameStrategy;
            this.importFileName = importFileName;
        }

        String ddlAuto; //none => do nothing, create => create db, create-drop => create and drop db per session.
        String dialect; //className of dialect for specific DBMS
        boolean showSql; //log sql requests

        //optional.
        String implicitNameStrategy; //className of ImplicitNamingStrategy
        String physicalNameStrategy; //className of PhysicalNamingStrategy
        String importFileName;
    };

    public static final int JPA_EMPTY_DB = 0;

    List<HibernateProperties> props;


    @Value("${jpa.hibernate.enhancer.dirtyTracking}")
    boolean dirtyTracking;
    @Value("${jpa.hibernate.enhancer.lazyInitialization}")
    boolean lazyInitialization;
    @Value("${jpa.hibernate.enhancer.associationManagement}")
    boolean associationManagement;
}
