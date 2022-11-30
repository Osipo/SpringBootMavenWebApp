package org.example.data.db;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectSqlDbWorker extends BaseDbWorker implements IDbWorker {

    public DirectSqlDbWorker(){super(null, null);}

    public DirectSqlDbWorker(String connection, JdbcTemplate jdbc){
        super(connection, jdbc);
    }

    @Override
    public List<Map<String, Object>> query(String sql){
        return jdbc.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> queryId(String sql, Integer id){
        return jdbc.queryForList(sql, (PreparedStatementSetter) preparedStatement -> preparedStatement.setInt(1, id));
    }


    @Override
    public List<Map<String, Object>> queryList() {
        return jdbc.queryForList("select * from Specifications (nolock) order by tid ASC ");
    }

    @Override
    public List<Map<String, Object>> queryList(Integer id) {
        return jdbc.queryForList("select * from Specifications (nolock) where tid = ?", new Object[]{id});
    }
}