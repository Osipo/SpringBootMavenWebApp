package org.example.builders;

import org.example.data.db.IDbWorker;

import javax.sql.DataSource;

public interface IDbWorkerBuilder {
    IDbWorker build(String connection, DataSource ds);
    IDbWorker buildFrom(IDbWorker empty, String connection, DataSource ds);
}
