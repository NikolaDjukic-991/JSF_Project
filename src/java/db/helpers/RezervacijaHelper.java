/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.Dan;
import db.Festival;
import db.Festivalgrupa;
import db.Rezervacija;
import java.util.Calendar;
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
public class RezervacijaHelper {
    private Session session = null;
    
    
    public List<Rezervacija> getReservacijeForUser(String uname){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Rezervacija> rezervacije = Collections.EMPTY_LIST;
        Transaction tx = session.beginTransaction();
        try{
            Rezervacija r;
            
            Criteria c = session.createCriteria(Rezervacija.class, "rezervacija");
            c.add(Restrictions.eq("rezervacija.korisnik.uname", uname));
            c.add(Restrictions.eq("status", 0));
            rezervacije = c.list();
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return rezervacije;
        
    }

    public boolean successfulReservation(Integer idRez) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        boolean ret = false;
        try{
            Criteria c = session.createCriteria(Rezervacija.class);
            c.add(Restrictions.eq("id", idRez));
            c.add(Restrictions.eq("status", 0));
            Rezervacija r = (Rezervacija) c.uniqueResult();
            r.setStatus(1);
            session.saveOrUpdate(r);
            tx.commit();
            ret = true;
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        
        return ret;
    }

    public void updateDans(Rezervacija rez) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Festival f = rez.getDan().getFestival();
            for(Dan d : f.getDans()){
                d.setBrRezervisanihKarata(d.getBrRezervisanihKarata() - rez.getBrojkarata());
                d.setBrProdatihKarata(d.getBrProdatihKarata() + rez.getBrojkarata());
            }
            session.saveOrUpdate(f);
            rez.setStatus(1);
            session.saveOrUpdate(rez);
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
    }

    public void updateDan(Rezervacija rez) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Dan d = rez.getDan();
            d.setBrRezervisanihKarata(d.getBrRezervisanihKarata() - rez.getBrojkarata());
            d.setBrProdatihKarata(d.getBrProdatihKarata() + rez.getBrojkarata());
            rez.setStatus(1);
            session.saveOrUpdate(rez);
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Rezervacija> getIstekleRezervacije() {
        List<Rezervacija> rezervacije = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Rezervacija.class);
            c.add(Restrictions.eq("status", 2));
            rezervacije = c.list();
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return rezervacije;
    }
    
    public void updateRezervacija(Rezervacija r) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            session.update(r);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void updateIstekleRezervacije() {
        Date datum = new Date();
        Date pre2dana = new Date(datum.getTime() - (2 * 24 * 3600 * 1000l));
        
        
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Rezervacija.class);
            c.add(Restrictions.eq("status", 0));
            c.add(Restrictions.le("datum", pre2dana));
            List<Rezervacija> rezervacije = c.list();
            for(Rezervacija r : rezervacije){
                r.setStatus(1);
                session.update(r);
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public Rezervacija getRezervacijaById(Integer idRez) {
        Rezervacija rez = null;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Rezervacija.class);
            c.add(Restrictions.eq("id", idRez));
            rez = (Rezervacija) c.uniqueResult();
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return rez;
    }

    
}
