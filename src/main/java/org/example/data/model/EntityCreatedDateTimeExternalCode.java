package org.example.data.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public class EntityCreatedDateTimeExternalCode extends EntityCreatedDateTime {

    @Type(type = "nstring")
    @Column(length = 300)
    String externalCode;
}