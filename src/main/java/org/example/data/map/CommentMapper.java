package org.example.data.map;

import org.example.data.db.IDbWorker;
import org.example.data.dto.CommentDto;
import org.example.managers.IDbWorkerManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentMapper implements IMapper<CommentDto> {

    @Override
    public List<CommentDto> map(List<Map<String, Object>> rows) {

        String content;
        LocalDateTime createdAt;
        List<CommentDto> result = new ArrayList<>();


        for(Map<String, Object> row: rows){
            content = (String) row.get("Content");
            createdAt = MapperUtils.toLocalDateTime(row.get("CreatedAt"));
            result.add(new CommentDto(content, createdAt));
        }
        return result;
    }
}