package org.example.builders;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MsSqlQueryBuilder {
    JdbcTemplate jdbc;

    public MsSqlQueryBuilder(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public String build(){
        return null;
    }

    public MsSqlQueryBuilder select(String columns){
        return this;
    }

    public MsSqlQueryBuilder from(String tableName){
        return this;
    }
    public MsSqlQueryBuilder where(String condition){
        return this;
    }
}