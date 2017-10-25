/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.Korisnik;
import db.Poruka;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class KorisnikHelper {
    private Session session = null;
    
    public KorisnikHelper(){
    }
    
    public Korisnik getKorisnik(String uname){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Korisnik k = null;
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Query q = session.createQuery("from Korisnik as korisnik where korisnik.uname = '"+uname+"'");
            List l = q.list();
            k = (Korisnik) q.uniqueResult();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return k;
    }
    
    public Korisnik getKorisnik(String uname, String lozinka){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Korisnik k = null;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            
            Query q = session.createQuery("from Korisnik as korisnik where korisnik.uname = '"+uname+"' AND korisnik.lozinka = '"+lozinka+"'");
            
            k = (Korisnik) q.uniqueResult();
            k.setLastLogin(new Date());
            session.update(k);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return k;
    }
    
    public boolean addKorisnik(Korisnik k){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean ret = false;
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Query q = session.createQuery("from Korisnik as korisnik where korisnik.uname = '"+k.getUname()+"'");
            if(q.uniqueResult() == null){
                session.saveOrUpdate(k);
                ret = true;
            }
            tx.commit();
        } catch (Exception e){
            ret = false;
            tx.rollback();
        }
        return ret;
    }
    
    public List<Korisnik> getKorisniciWithType(int type){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Korisnik> korisnici = Collections.EMPTY_LIST;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Korisnik.class);
            c.add(Restrictions.eq("tip", type));
            
            korisnici = c.list();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
        return korisnici;
    }

    public void deleteKorisnik(Korisnik k) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            session.delete(k);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void updateKorisnikTimeStamp(Korisnik korisnik) {
        korisnik.setLastLogin(new Date());
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            session.update(korisnik);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
    }
    
    
    public List<Korisnik> getRecent(){
        List<Korisnik> korisnici = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Korisnik.class);
            c.addOrder(Order.desc("lastLogin"));
            c.setMaxResults(10);
            korisnici = c.list();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return korisnici;
    }

    public void updateKorisnik(Korisnik k) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            session.update(k);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Poruka> getMessagesForUser(Korisnik korisnik) {
        List<Poruka> msgs = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Poruka.class);
            Poruka p;
            c.add(Restrictions.eq("korisnik", korisnik));
            c.addOrder(Order.desc("datum"));
            msgs = c.list();
            
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
        return msgs;
    }

    public void removeMessages(List<Poruka> messages) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            for(Poruka p : messages){
                session.delete(p);
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void addMessage(Poruka p) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            session.saveOrUpdate(p);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }
}
