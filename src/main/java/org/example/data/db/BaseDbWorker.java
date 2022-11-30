package org.example.data.db;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDbWorker {
    protected String connection;
    protected JdbcTemplate jdbc;

    public BaseDbWorker(String connection, JdbcTemplate jdbc){
        this.connection = connection;
        this.jdbc = jdbc;
    }
}
