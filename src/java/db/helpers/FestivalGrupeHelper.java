/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.Festivalgrupa;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class FestivalGrupeHelper {
    private Session session = null;
    
    
    public List<Festivalgrupa> getFestivalGrupe(){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Festivalgrupa> festivalGrupe = Collections.EMPTY_LIST;
       
        Transaction tx = session.beginTransaction();
        try{
            Query q = session.createQuery("from Festivalgrupa");
            festivalGrupe = q.list();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        return festivalGrupe;
        
    }

    public Festivalgrupa dodajFestivalGrupu(Festivalgrupa festivalgrupa) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
       
        Transaction tx = session.beginTransaction();
        try{
            session.save(festivalgrupa);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        
        return festivalgrupa; // mozda nece id postaviti.
    }
    
    public Festivalgrupa getFestivalGrupaWithId(Integer id){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Festivalgrupa grupa = null;
       
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Festivalgrupa.class);
            c.add(Restrictions.eq("idgrupe", id));
            grupa = (Festivalgrupa) c.uniqueResult();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        return grupa;
        
    }
}
