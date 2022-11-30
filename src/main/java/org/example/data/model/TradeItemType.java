package org.example.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "TradeItemType")
@Table(name = "TradeItemTypes", indexes = @Index(name = "UIX_TradeItemTypes_exCode", columnList = "externalCode", unique = true))
public class TradeItemType extends EntityCreatedDateTimeExternalCode implements Serializable {

    @Id
    @GeneratedValue(
            //generator = "tradetype_sequence",
            strategy = GenerationType.IDENTITY
    )
    /*
    @GenericGenerator(
            name = "tradetype_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "seq_tradeitemtype_ids"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "5"),
                    @Parameter(name = "optimizer", value = "pooled-lo")
            }
    )*/
    Long tid;

    @Type(type = "nstring")
    @Column(length = 300)
    String nameRU;



    //By PK tid.
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        TradeItemType b = (TradeItemType) o;
        return Objects.equals(this.tid, b.tid);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.tid);
    }
}
