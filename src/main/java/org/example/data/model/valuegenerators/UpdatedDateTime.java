package org.example.data.model.valuegenerators;

import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ValueGenerationType(generatedBy = UpdatedAtDateTimeGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatedDateTime {
}
