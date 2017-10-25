/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.Izvodjac;
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
public class IzvodjacHelper {
    private Session session = null;
    public List<Izvodjac> getIzvodjaci() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        List<Izvodjac> izvodjaci = null;
       
        Transaction tx = session.beginTransaction();
        try{
            
            Query q = session.createQuery("from Izvodjac");
            izvodjaci = q.list();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        return izvodjaci;
    }

    public void dodajIzvodjaca(Izvodjac izvodjac) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            session.save(izvodjac);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }

    }
    
    public boolean proveriJelPostoji(String ime){
        boolean postoji = false;
        
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Izvodjac.class);
            c.add(Restrictions.eq("ime", ime));
            postoji = !c.list().isEmpty();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        
        return postoji;
    }
    
    public Izvodjac dohvatiIzvodjaca(String ime){
        Izvodjac izv = null;
        
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Izvodjac.class);
            c.add(Restrictions.eq("ime", ime));
            izv = (Izvodjac) c.uniqueResult();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }
        
        return izv;
    }
    
}
