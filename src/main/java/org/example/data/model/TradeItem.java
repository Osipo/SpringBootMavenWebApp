package org.example.data.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;


import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "TradeItem")
@Table(name = "TradeItems", indexes = @Index(name = "UIX_TradeItems_ean128", columnList = "ean128", unique = true))
public class TradeItem extends EntityCreatedDateTime implements Serializable {

    @Id
    @GeneratedValue(
            //generator = "trade_sequence",
            strategy = GenerationType.IDENTITY
    )
    /*
    @GenericGenerator(
            name = "trade_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "seq_tradeitem_ids"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "10"),
                    @Parameter(name = "optimizer", value = "pooled-lo")
            }
    )
    */
    Long tid;

    @Type(type = "nstring")
    @Column(name = "nameRU", length = 300)
    String name;

    @Type(type = "nstring")
    @Column(name = "ean128", length = 50)
    String ean128;

    @Column(name = "IsInStock", nullable = false)
    boolean isAvailable;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TradeItemType_id", foreignKey = @ForeignKey(name = "FK_TradeItemType_id"))
    TradeItemType type;

    //attaching target to source. Some items are compatible with each others. (Many-to-Many).
    //This Many-to-Many is split into bi-directional One-to-Many/Many-to-One relation.
    @OneToMany(mappedBy = "sourceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompatibleTradeItems> compatibles = new ArrayList<>();

    public void addCompatible(TradeItem b){
        CompatibleTradeItems link = new CompatibleTradeItems(this, b);
        if(compatibles.contains(link))
            return;
        compatibles.add(link);
        b.addCompatible(this); //cyclic reference handled by check.
    }

    public void removeCompatible(TradeItem b){
        CompatibleTradeItems link = new CompatibleTradeItems(this, b);
        if(!compatibles.contains(link))
            return;
        compatibles.remove(link);
        b.removeCompatible(this); //cyclic reference handled by check.
    }

    //has naturalId > by NaturalId.
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        TradeItem b = (TradeItem) o;
        return Objects.equals(this.ean128, b.ean128);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.ean128);
    }
}