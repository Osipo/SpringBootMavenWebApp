package org.example.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.data.dao.TradeItemDao;
import org.example.data.dao.TradeItemService;
import org.example.data.dao.TradeItemTypeDao;
import org.example.data.dao.TradeItemTypeService;
import org.example.data.model.TradeItem;
import org.example.data.repository.TradeItemRepository;
import org.example.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeitemController extends BaseController {

    @Autowired
    TradeItemRepository tradeItemRepository;

    @Autowired
    TradeItemService tradeItemDao;
    @Autowired
    TradeItemTypeService tradeItemTypeDao;

    public TradeitemController(ApplicationProperties configuration) {
        super(configuration);
    }

    //3. JPA-Repository approach.

    @GetMapping(value = "/tradeitems", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public Iterable<TradeItem> getTestQueryList(){
        return tradeItemRepository.findAll();
    }


    @GetMapping(value = "/tradeitems2", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public Iterable<TradeItem> getTestQueryList2(){ return tradeItemDao.getAll(); }

    @GetMapping(value = "/tradeitem/{id}", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public TradeItem getTestQuery(@PathVariable("id") Long id){
        return tradeItemDao.getOne(id);
    }

    @GetMapping(value = "/tradeitem/delete/{id}", produces = {"application/json", "application/xml", "text/json", "text/xml"})
    public String deleteQuery(@PathVariable("id") Long id){
        tradeItemTypeDao.deleteById(id);
        return "deleted " + id;
    }

    @PostConstruct
    public void fillData(){
        tradeItemTypeDao.buildSome();
        tradeItemDao.buildSome();
    }
}