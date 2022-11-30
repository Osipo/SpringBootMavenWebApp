package org.example.data.db;

import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Service
public interface IDbWorker {
    List<Map<String, Object>> query(String sql);
    List<Map<String, Object>> queryId(String sql, Integer id);


    //TEST METHODS
    List<Map<String, Object>>  queryList();
    List<Map<String, Object>>  queryList(Integer id);
}
