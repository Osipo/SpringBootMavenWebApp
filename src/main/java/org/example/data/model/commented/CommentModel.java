package org.example.data.model.commented;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "Comments")
public class CommentModel {

    @Id
    UUID commentId;

    @Type(type = "nstring") //map to NVARCHAR
    String content;

    @Column(columnDefinition = "DATETIME")
    LocalDateTime createdAt;

    @Column(columnDefinition = "DATETIME")
    LocalDateTime updatedAt;
}
