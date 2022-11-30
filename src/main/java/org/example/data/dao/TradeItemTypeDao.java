package org.example.data.dao;

import org.example.data.model.TradeItemType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class TradeItemTypeDao extends BaseHibernateDao<TradeItemType> {

    public TradeItemTypeDao(EntityManagerFactory managerFactory) {
        super(managerFactory, TradeItemType.class);
    }


    public List<TradeItemType> getAll(){
        return super.findAll();
    }



    public void setExternalCode(long id, String code){
        Session s = getCurrentSession();
        try {
            Transaction tr = s.beginTransaction();

            TradeItemType e = findOne(id);
            e.setExternalCode(code);
            update(e);

            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
        }
    }

    public void deleteById(long id){
        Session s = getCurrentSession();
        try{
            Transaction tr = s.beginTransaction();
            super.deleteById(id);
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

            TradeItemType e1 = new TradeItemType();
            e1.setNameRU("Technique");
            e1.setExternalCode("TECH");

            TradeItemType e2 = new TradeItemType();
            e2.setNameRU("Gadget");
            e2.setExternalCode("GDT");

            create(e1);
            create(e2);

            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
        }
    }
}