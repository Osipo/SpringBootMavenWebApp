package org.example.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "db")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("db_props")
public class DbProperties {

    public static final int SQL_SERVER = 0;
    public static final int SQL_SERVER_WINDOWS = 2; //[1, 2].
    public static final int H2_CONSOLE = 3;
    public static final int H2_TCP = 4;

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class DbConnection {

        @ConstructorBinding
        public DbConnection(String protocol, String driverClassName, String ip, String catalog, String login, String password, boolean integratedSecurity){
            this.protocol = protocol;
            this.driverClassName = driverClassName;

            this.ip = ip;
            this.catalog = catalog;
            this.login = login;
            this.password = password;
            this.integratedSecurity = integratedSecurity;
        }

        String ip;
        String catalog;
        String login;
        String password;

        //JDBC properties.
        String protocol;
        String driverClassName;

        //For some driver or providers. this property has different names and values
        //OracleClient has ('yes', 'no') instead of ('true', 'false')
        //OleDb has 'SSPI' as true
        //Odbc has another name: 'Trusted_Connection'
        boolean integratedSecurity;

        public String getIntegratedSecurity(){
            if(protocol.toLowerCase().contains("oracle") || driverClassName.toLowerCase().contains("oracle")) //oracle
                return (integratedSecurity) ? "integratedSecurity=yes" : "integratedSecurity=no";
            else if(protocol.toLowerCase().contains("oledb") || driverClassName.toLowerCase().contains("oledb")) //oledb
                return (integratedSecurity) ? "integratedSecurity=sspi" : "integratedSecurity=false";
            else if(protocol.toLowerCase().contains("odbc") || driverClassName.toLowerCase().contains("odbc")) //odbc
                return (integratedSecurity) ? "trustedConnection=yes" : "trustedConnection=no";
            else
                return (integratedSecurity) ? "integratedSecurity=true;authenticationScheme=nativeAuthentication" : "integratedSecurity=false";  //sqlserver
        }
    }

    List<DbConnection> connections;
}