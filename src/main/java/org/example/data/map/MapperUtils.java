package org.example.data.map;

import org.example.exceptions.MappingException;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class MapperUtils {
    public static LocalDateTime toLocalDateTime(Object v){
        if(v instanceof Timestamp)
            return ((Timestamp) v).toLocalDateTime();
        else
            throw new MappingException("toLocalDateTime(): cannot map type " + v.getClass().getName() + " into java.time.LocalDateTime");
    }
}
