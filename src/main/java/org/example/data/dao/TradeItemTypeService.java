package org.example.data.dao;

import org.example.data.model.TradeItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeItemTypeService {

    @Autowired
    TradeItemTypeDao dao;

    public List<TradeItemType> getAll(){
        return dao.getAll();
    }

    public void setExternalCode(long id, String code){
        dao.setExternalCode(id, code);
    }
    public void buildSome(){
        dao.buildSome();
    }
    public void deleteById(long id){dao.deleteById(id);}
}
