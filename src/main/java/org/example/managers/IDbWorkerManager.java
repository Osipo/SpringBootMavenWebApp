package org.example.managers;

import org.example.builders.IDbWorkerBuilder;
import org.example.builders.ISqlQueryBuilder;
import org.example.data.db.IDbWorker;
import org.example.properties.DbProperties;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public interface IDbWorkerManager {
    void setConnection(IDbWorkerBuilder builder, DbProperties.DbConnection connection);

    DataSource getDataSourceFromConnection(DbProperties.DbConnection connection);


    IDbWorker getDbWorker();
    ISqlQueryBuilder getQueryBuilder();
}
