package org.example.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "TradeItemCompatibles")
@Data
@AssociationOverrides({
        @AssociationOverride(
                name = "sourceItem.tid",
                joinColumns = @JoinColumn(name = "SourceItem_id")
        ),
        @AssociationOverride(
                name = "targetItem.tid",
                joinColumns = @JoinColumn(name = "TargetItem_id")
        )
})
public class CompatibleTradeItems {

    CompatibleTradeItems(TradeItem sourceItem, TradeItem targetItem){
        this.sourceItem = sourceItem;
        this.targetItem = targetItem;
    }

    //surrogate key for clustered PK
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    Long tid;

    @ManyToOne
    TradeItem sourceItem;
    @ManyToOne
    TradeItem targetItem;

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        CompatibleTradeItems b = (CompatibleTradeItems) o;
        return Objects.equals(this.sourceItem, b.sourceItem) && Objects.equals(this.targetItem, b.targetItem);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.sourceItem, this.targetItem);
    }
}