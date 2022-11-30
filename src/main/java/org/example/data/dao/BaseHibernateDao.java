package org.example.data.dao;

import org.example.exceptions.EntityNotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;


import java.io.Serializable;
import java.util.List;

public class BaseHibernateDao<T extends Serializable> {


    protected EntityManagerFactory managerFactory;

    protected SessionFactory sessionFactory;

    private Class<T> clazz;


    @Autowired
    public BaseHibernateDao(EntityManagerFactory managerFactory,  Class<T> clazz){
        if(managerFactory == null)
            throw new IllegalArgumentException("Cannot set managerFactory to null!");
        if(clazz == null)
            throw new IllegalArgumentException("Cannot set class of dao to null!");

        this.managerFactory = managerFactory;
        this.sessionFactory = managerFactory.unwrap(SessionFactory.class);
        this.clazz = clazz;
    }

    // API
    public T findOne(final long id) {
        Session s = getCurrentSession();
        T entity = null;
        try{
            Transaction tr = s.beginTransaction();
            entity = s.get(clazz, id);
            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
        }
        if(entity == null) {
            throw new EntityNotFoundException("Cannot find entity with id = " + id);
        }
        return entity;
    }


    public List<T> findAll() {
        Session s = getCurrentSession();
        List<T> result = null;
        try {
            Transaction tr = s.beginTransaction();
            result = s.createQuery("from " + clazz.getName()).list();
            tr.commit();
        } catch (Exception e){
            if(s.getTransaction().getStatus() == TransactionStatus.ACTIVE || s.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                s.getTransaction().rollback();
            result = List.of(); //empty list.
        }
        return result;
    }


    public T create(final T entity) {
        if(entity == null)
            throw new IllegalArgumentException("Inserted Entity is null!");

       getCurrentSession().saveOrUpdate(entity);
       return entity;
        /*
        return tr.execute((status) -> {
            getCurrentSession().saveOrUpdate(entity);
            return entity;
        });
         */
    }

    public T update(final T entity) {
        if(entity == null)
            throw new IllegalArgumentException("Updated Entity is null!");

        return (T) getCurrentSession().merge(entity);
        /*
        return tr.execute((status) -> {
            return (T) getCurrentSession().merge(entity);
        });
        */
    }

    public void delete(final T entity) {
        if(entity == null)
            throw new IllegalArgumentException("Deleted Entity is null!");

        getCurrentSession().delete(entity);
        /*
        tr.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                getCurrentSession().delete(entity);
            }
        });
        */
    }


    public void deleteById(final long entityId) {
        final T entity = findOne(entityId);
        if(entity == null)
            throw new EntityNotFoundException("Cannot find entity with id = " + entityId);
        delete(entity);
    }

    protected Session getCurrentSession(){
        Session s = null;
        try{
            s = this.sessionFactory.getCurrentSession();
            System.out.println("successful got  CurrentSession");
        } catch (HibernateException e){
            System.err.println(e.getMessage());
            System.err.println("cannot getCurrentSession(). Initiate new openSession()");
            s = this.sessionFactory.openSession();
        }
        return s;
    }
}
