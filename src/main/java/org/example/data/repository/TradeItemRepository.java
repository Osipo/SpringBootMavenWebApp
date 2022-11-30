package org.example.data.repository;

import org.example.data.model.TradeItem;
import org.springframework.data.repository.CrudRepository;

public interface TradeItemRepository extends CrudRepository<TradeItem, Integer>
{
}