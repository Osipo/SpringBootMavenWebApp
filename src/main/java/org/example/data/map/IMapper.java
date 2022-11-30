package org.example.data.map;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IMapper<T> {
    List<T> map(List<Map<String, Object>> rows);
}