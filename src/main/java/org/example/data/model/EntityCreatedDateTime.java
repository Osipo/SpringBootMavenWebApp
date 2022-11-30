package org.example.data.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.data.model.valuegenerators.CreatedDateTime;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public class EntityCreatedDateTime {

    @Column(name = "RecordDate", columnDefinition = "DATETIME not null", updatable = false)
    @CreatedDateTime
    //@ColumnDefault("getdate()") //enforce default constraint in column definition.
    LocalDateTime createdAt;
}