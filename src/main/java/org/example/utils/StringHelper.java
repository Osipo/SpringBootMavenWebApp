package org.example.utils;

import java.util.List;

public class StringHelper {

    public static String subtractFrom(String source, String sub){
        if(source == null || sub == null)
            return null;

        if(source.contains(sub)){
            return source.substring(0, source.lastIndexOf(sub));
        }
        else
            return null;
    }

    public static String iterableToString(Iterable<?> elems){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Object e : elems){
            sb.append(e.toString());
            sb.append(", ");
        }

        //remove last ', '
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(']');
        return sb.toString();
    }
}
