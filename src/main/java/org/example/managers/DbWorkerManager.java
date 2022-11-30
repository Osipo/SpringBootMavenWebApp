package org.example.managers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.builders.IDbWorkerBuilder;
import org.example.builders.ISqlQueryBuilder;
import org.example.data.db.IDbWorker;
import org.example.properties.DbProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component("db_manager")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DbWorkerManager implements IDbWorkerManager {

    IDbWorker dbWorker;
    ISqlQueryBuilder queryBuilder;

    @Override
    public IDbWorker getDbWorker() {
        return dbWorker;
    }

    @Override
    public ISqlQueryBuilder getQueryBuilder(){return queryBuilder;}

    @Override
    public void setConnection(IDbWorkerBuilder builder, DbProperties.DbConnection connection) {
        String url = checkConnection(connection);
        this.dbWorker = builder.build(url, getDataSourceFromConnection(connection));
        System.out.println("Successful created IDbWorker with url = '" + url + "'");
    }

    @Override
    public DataSource getDataSourceFromConnection(DbProperties.DbConnection connection) {

        String connectionString = checkConnection(connection);
        if(connectionString == null || connectionString.length() <= 0){
            System.err.println("IDbWorkerManager: Cannot change IDbWorker connection. Connection String required.");
            return null;
        }


        final String userName = connection.getLogin();
        final String password = connection.getPassword();
        final String driver = connection.getDriverClassName();

        return DataSourceBuilder
                .create()
                .driverClassName(driver)
                .url(connectionString)
                .username(userName)
                .password(password)
                .build();
    }


    public static String checkConnection(DbProperties.DbConnection connection){
        if(connection == null)
            return null;

        StringBuilder sb = new StringBuilder();

        String protocol = connection.getProtocol();
        if(protocol.equals("jdbc:sqlserver")){
            sb.append(protocol).append("://");
            sb.append(connection.getIp()).append(";");
            sb.append("databaseName=").append(connection.getCatalog()).append(";");
            sb.append(connection.getIntegratedSecurity());
        }
        else if(protocol.equals("jdbc:h2:tcp")){
            sb.append(protocol).append("://");
            sb.append(connection.getIp()).append('/');
            sb.append(connection.getCatalog());
        }
        else if(protocol.equals("jdbc:h2:mem")){
            sb.append(protocol).append(':');
            sb.append(connection.getCatalog());
        }
        return sb.toString();
    }
}