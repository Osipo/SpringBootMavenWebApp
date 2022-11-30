package org.example.data.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentDto {
    String content;
    LocalDateTime createdAt;
}