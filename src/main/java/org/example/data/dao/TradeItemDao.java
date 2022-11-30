package org.example.data.dao;

import org.example.data.model.TradeItem;
import org.example.data.model.TradeItemType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class TradeItemDao extends BaseHibernateDao<TradeItem> {

    public TradeItemDao(EntityManagerFactory managerFactory) {
        super(managerFactory, TradeItem.class);
    }


    public List<TradeItem> getAll(){
        return super.findAll();
    }

    public TradeItem getOne(long id){ return super.findOne(id);}

    public void setName(long id, String name){
        Session s = getCurrentSession();
        try {
            Transaction tr = s.beginTransaction();

            TradeItem e = super.findOne(id);
            e.setName(name);
            update(e);

            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
        }
    }



    public void buildSome(){
        Session s = getCurrentSession();
        try {
            Transaction tr = s.beginTransaction();

            TradeItem e1 = new TradeItem();
            e1.setName("Asus vevo");
            e1.setEan128("1234554321");
            e1.setType(getCurrentSession().load(TradeItemType.class, 1L));
            e1.setAvailable(true);

            TradeItem e2 = new TradeItem();
            e2.setName("Acer-expire");
            e2.setEan128("67890000");
            e2.setType(null);
            //e2.setAvailable(false);

            TradeItem e3 = new TradeItem();
            e3.setName("Hirbis-Pro");
            e3.setEan128("333333333");
            e3.setType(getCurrentSession().load(TradeItemType.class, 2L));
            e3.setAvailable(true);

            create(e1);
            create(e2);
            create(e3);

            e1.addCompatible(e3); //(1, 3), (3, 1) inserted.

            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
        }
    }
}