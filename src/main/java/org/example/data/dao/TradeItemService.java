package org.example.data.dao;

import org.example.data.model.TradeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TradeItemService {

    @Autowired
    TradeItemDao dao;

    public List<TradeItem> getAll(){
        return dao.getAll();
    }
    public void setName(long id, String name){
        dao.setName(id, name);
    }
    public void buildSome(){
        dao.buildSome();
    }
    public TradeItem getOne(long id){return dao.getOne(id);}
}
