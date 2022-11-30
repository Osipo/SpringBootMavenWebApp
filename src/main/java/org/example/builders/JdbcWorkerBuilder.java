package org.example.builders;

import org.example.data.db.DirectSqlDbWorker;
import org.example.data.db.IDbWorker;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JdbcWorkerBuilder implements IDbWorkerBuilder {

    @Override
    public IDbWorker build(String connection, DataSource ds) {
        return new DirectSqlDbWorker(connection, new JdbcTemplate(ds));
    }

    @Override
    public IDbWorker buildFrom(IDbWorker empty, String connection, DataSource ds) {
        Constructor<?> worker_ctor = null;
        IDbWorker new_worker = null;
        try{
            worker_ctor = empty.getClass().getConstructor(String.class, JdbcTemplate.class);
        } catch (NoSuchMethodException e){
            System.err.println("JdbcWorkerBuilder: Cannot change worker to the new connection. Constructor of IDbWorker not found");
            return null;
        }

        try{
            new_worker = (IDbWorker) worker_ctor.newInstance(connection, new JdbcTemplate(ds));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e){
            System.err.println("IDbWorkerManager: Cannot create new worker for new connection.");
            return null;
        }

        return new_worker;
    }
}
