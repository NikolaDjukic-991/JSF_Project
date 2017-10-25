/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.display.FestivalDisplay;
import com.corejsf.hibernate.HibernateUtil;
import db.Dan;
import db.DanId;
import db.Festival;
import db.Izvodjac;
import db.Komentar;
import db.KomentarId;
import db.Korisnik;
import db.Media;
import db.MediaId;
import db.Nastupa;
import db.NastupaId;
import db.Poruka;
import db.Rezervacija;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class FestivalHelper {
    private Session session = null;
    private Korisnik neregistrovani = null;
    
    public FestivalHelper(){
        if(neregistrovani == null){
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            try{
                Criteria c = session.createCriteria(Korisnik.class);
                c.add(Restrictions.eq("uname", "neregistrovani"));
                neregistrovani = (Korisnik) c.uniqueResult();
                tx.commit();
            }catch(HibernateException e){
                tx.rollback();
                e.printStackTrace();
            }
        }
    }
    
    public List<Festival> getMostRecent(){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Date date = new Date();
        List<Festival> festivali = Collections.EMPTY_LIST;
       
        Transaction tx = session.beginTransaction();
        try{
            Criteria criteria = session.createCriteria(Festival.class);
            criteria.add(Restrictions.ge("do_", date));
            criteria.add(Restrictions.eq("status", 0));
            criteria.addOrder(Order.asc("do_"));
            criteria.setMaxResults(5);
            festivali = criteria.list();
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return festivali;
        
    }
    
    public List<Festival> getTopRated(){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Date date = new Date();
        List<Festival> festivali = Collections.EMPTY_LIST;
       
        Transaction tx = session.beginTransaction();
        try{
            Criteria criteria = session.createCriteria(Festival.class);
            criteria.add(Restrictions.ge("do_", date));
            criteria.add(Restrictions.isNotNull("komentars"));
            criteria.add(Restrictions.eq("status", 0));
            festivali = criteria.list();
            
            
            festivali.sort((Festival f1, Festival f2) -> {
                int nf1 = 0, sumf1 = 0;
                int nf2 = 0, sumf2 = 0;
                Set<Festival> festivali1 = f1.getFestivalgrupa().getFestivals();
                Set<Festival> festivali2 = f1.getFestivalgrupa().getFestivals();
                
                for(Festival ff1 : festivali1){
                    for(Komentar k : ff1.getKomentars()){
                        if(k.getOcena() != null){
                            nf1++;
                            sumf1 += k.getOcena();
                        }
                    }
                }
                
                for(Festival ff2 : festivali2){
                    for(Komentar k : ff2.getKomentars()){
                        if(k.getOcena() != null){
                            nf1++;
                            sumf1 += k.getOcena();
                        }
                    }
                }
                
                int ret;
                if(nf1 != 0 && nf2 != 0){
                    Double avgf1 = new Double( sumf1 / nf1 );
                    Double avgf2 = new Double( sumf2 / nf2 );
                    ret = avgf2.compareTo(avgf1);
                    
                } else {
                    ret = 0;
                }
                return ret;
            });
            
            List<Festival> najpet = new ArrayList<>();
            for(int i = 0; (i < festivali.size() && i < 5); i++){
                najpet.add(festivali.get(i));
            }
            festivali = najpet;
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        
        return festivali;
        
    }
    
    
    public List<Festival> search(List<Izvodjac> izabraniIzvodjaci,
                                    Date datumOd,
                                    Date datumDo,
                                    String naziv,
                                    String mesto){

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Festival> festivali = null;
       
        Transaction tx = session.beginTransaction();
        try{
            Criteria criteria = session.createCriteria(Festival.class);
            if(naziv != null)
                if(!naziv.equals(""))
                    criteria.add(Restrictions.like("ime", "%"+naziv+"%"));
            
            if(mesto != null)
                if(!mesto.equals(""))
                    criteria.add(Restrictions.like("mesto", "%"+mesto+"%"));
            
            if(datumOd != null){
                criteria.add(Restrictions.ge("od", datumOd));
            }
            
            if(datumDo != null){
                criteria.add(Restrictions.le("do_", datumDo));
            }
            if(datumOd == null && datumDo == null){
                criteria.add(Restrictions.ge("do_", new Date()));
            }

            if(izabraniIzvodjaci != null){
                if(!izabraniIzvodjaci.isEmpty()){
                    criteria.add(Restrictions.in("izvodjac", izabraniIzvodjaci));
                }
            }
            
            criteria.add(Restrictions.eq("status", 0));
            
            festivali = criteria.list();
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return festivali;
        
    }

    public List<Festival> getFestivalsOnDate(Date date) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Festival> festivali = null;
        
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.le("od", date));
            c.add(Restrictions.ge("do_", date));
            c.add(Restrictions.eq("status", 0));
            festivali = c.list();
            tx.commit();
        }
        catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return festivali;
    }

    public void dodajFestival(Festival festival, List<Media> media, List<Dan> dans) {
        festival.setStatus(0);
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            
            Integer idfest = (Integer) session.save(festival);
            int i = 0;
            for(Media m : media){
                m.setFestival(festival);
                m.setId(new MediaId(idfest, i++));
                session.save(m);
            }
            i=0;
            for(Dan d : dans){
                d.setFestival(festival);
                d.setId(new DanId(idfest, i++));
                Set<Nastupa> nastupas = d.getNastupas();
                d.setNastupas(Collections.EMPTY_SET);
                DanId idDan = (DanId) session.save(d);
                for(Nastupa n : nastupas){
                    n.setDan(d);
                    n.setId(new NastupaId(n.getIzvodjac().getIdIzvodjac(), idDan.getIdFest(), idDan.getIdDan()));
                    session.save(n);
                }
                d.setNastupas(nastupas);
                session.save(d);
            }
            festival.setDans(new HashSet(dans));
            festival.setMedias(new HashSet(media));
            
            session.save(festival);
            
            tx.commit();
            
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Festival> getMostViewed() {
        List<Festival> festivali = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.ge("do_", new Date()));
            c.add(Restrictions.eq("status", 0));
            c.addOrder(Order.desc("brojacpregleda"));
            c.setMaxResults(5);
            festivali = c.list();
            tx.commit();
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
        return festivali;
    }

    public List<Festival> getMostBought() {
        List<Festival> festivali = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            List<Festival> festivali_unfiltered;
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.eq("status", 0));
            c.add(Restrictions.ge("do_", new Date()));
            festivali_unfiltered = c.list();
            
            festivali_unfiltered.sort((festival1, festival2) -> {
                Integer f1bought = 0, f2bought = 0;
                for(Dan d : festival1.getDans()){
                    for(Rezervacija r : d.getRezervacijas()){
                        if(r.getStatus() == 1) f1bought++;
                    }
                }
                for(Dan d : festival2.getDans()){
                    for(Rezervacija r : d.getRezervacijas()){
                        if(r.getStatus() == 1) f2bought++;
                    }
                }
                return f2bought.compareTo(f1bought);
            });
            festivali = new ArrayList<>();
            for(Festival f : festivali_unfiltered){
                festivali.add(f);
            }
            tx.commit();
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
        return festivali;
    }

    public int kupi(Dan izabranDan, int i, int brKarata) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            if(izabranDan.getBrKarata() - (izabranDan.getBrProdatihKarata() + izabranDan.getBrRezervisanihKarata()) < brKarata){
                tx.rollback();
                return -1;
            }
            
            Rezervacija r = new Rezervacija(izabranDan, neregistrovani, 1, 1, new Date(), brKarata);
            izabranDan.getRezervacijas().add(r);
            izabranDan.setBrKarata(izabranDan.getBrProdatihKarata()+brKarata);
            
            session.persist(r);
            session.saveOrUpdate(izabranDan);
            tx.commit();
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return 0;
    }
    
    public Festival getFestivalById(int idFest){
        Festival f = null;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.eq("idFest", idFest));
            f = (Festival) c.uniqueResult();
            tx.commit();
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return f;
    }

    public int rezervisiPaket(Festival fest, Korisnik korisnik, int brKarata) {
        boolean directSell = false;
        if(korisnik == null){
            korisnik = neregistrovani;
            directSell = true;
        }
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Dan dan0 = null;
        try{
            if((fest.getBrKarataPoKorisniku() < brKarata) && !directSell){
                tx.rollback();
                return -1;
            }
            for(Dan d : fest.getDans()){
                if(d.getBrKarata() - (d.getBrProdatihKarata() + d.getBrRezervisanihKarata() + brKarata) < 0){
                    tx.rollback();
                    return -2;
                }
                if(d.getId().getIdDan() == 0){
                    dan0 = d;
                }
            }
            if(dan0 == null){
                tx.rollback();

                return -3;
            }
            if(korisnik == null){
                tx.rollback();
                return -4;
            }
            
            if(!directSell){
                for(Dan d : fest.getDans()){
                    for(Rezervacija r : d.getRezervacijas()){
                        if(r.getKorisnik().getUname().equals(korisnik.getUname())){
                            tx.rollback();
                            return -5;
                        }
                    }
                }
            }
            
            Rezervacija r = new Rezervacija(dan0, korisnik, 1, 0, new Date(), brKarata);
            if(directSell){
                r.setStatus(1);
                for(Dan d : fest.getDans()){
                    d.setBrProdatihKarata(d.getBrProdatihKarata()+ brKarata);
                    session.update(d);
                }
            } else {
                for(Dan d : fest.getDans()){
                    d.setBrRezervisanihKarata(d.getBrRezervisanihKarata() + brKarata);
                    session.update(d);
                }
            }
            dan0.getRezervacijas().add(r);
            
            session.persist(r);
            
            tx.commit();
            
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return 0;
    }

    public int rezervisiDan(Dan dan, Korisnik korisnik, int brKarata) {
        boolean directSell = false;
        if(korisnik == null){
            korisnik = neregistrovani;
            directSell = true;
        }
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            if(dan == null){
                tx.rollback();

                return -3;
            }
            if( dan.getFestival().getBrKarataPoKorisniku() < brKarata){
                tx.rollback();
                return -1;
            }
            
            for(Dan d : dan.getFestival().getDans()){
                for(Rezervacija r : d.getRezervacijas()){
                    if(r.getKorisnik().getUname().equals(korisnik.getUname())){
                        tx.rollback();
                        return -5;
                    }
                }
            }
            
            
            if(dan.getBrKarata() - (dan.getBrProdatihKarata() + dan.getBrRezervisanihKarata() + brKarata) < 0){
                tx.rollback();

                return -2;
            }
            

            if(korisnik == null){
                tx.rollback();
                return -4;
            }
            Rezervacija r = new Rezervacija(dan, korisnik, 0, 0, new Date(), brKarata);
            if(directSell){
                r.setStatus(1);
                dan.setBrProdatihKarata(dan.getBrProdatihKarata()+r.getBrojkarata());
            } else {
                dan.setBrRezervisanihKarata(dan.getBrRezervisanihKarata() + brKarata);
            }
            dan.getRezervacijas().add(r);
            
            
            session.persist(r);
            session.update(dan);
            
            tx.commit();
            
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        return 0;
    }

    public void updateBrojacPregleda(Festival fest) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            fest.setBrojacpregleda(fest.getBrojacpregleda()+1);
            session.update(fest);
            tx.commit();
        }
        catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void revertRezervacija(Rezervacija r) {
        int tip = r.getTip();
        int brKarata = r.getBrojkarata();
        Festival f = r.getDan().getFestival();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            if(tip == 0){
                Dan d = r.getDan();
                d.setBrRezervisanihKarata(r.getDan().getBrRezervisanihKarata()-r.getBrojkarata());
                session.update(d);
            }
            if(tip == 1){
                for(Dan d : f.getDans()){
                    d.setBrRezervisanihKarata(d.getBrRezervisanihKarata()-r.getBrojkarata());
                    session.update(d);
                }
                
            }
            tx.commit();
        } catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Festival> getUpcomingFestivals() {
        List<Festival> festivali = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.ge("do_", new Date()));
            c.add(Restrictions.eq("status", 0));
            festivali = c.list();
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        
        return festivali;
    }
    
    public void cancelFestival(Festival f){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            f.setStatus(1);
            for(Dan d : f.getDans()){
                for(Rezervacija r : d.getRezervacijas()){
                    if(r.getStatus() == 0 || r.getStatus() == 1){
                        r.setStatus(4);
                        Korisnik k = r.getKorisnik();
                        Poruka p = new Poruka(k, "Festival "+f.getIme()+" je otkazan. Ukoliko ste kupili kartu, obratite se administraciji.", new Date());
                        session.save(p);
                        session.update(r);
                    }
                }
            }
            session.update(f);
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void addKomentar(Komentar k, Festival f) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Komentar.class);
            c.add(Restrictions.eq("festival", f));
            List<Komentar> list = (List<Komentar>)c.list();
            int max = 0;
            for(Komentar kk : list){
                if(kk.getId().getIdKomentar() > max) max = kk.getId().getIdKomentar();
            }
            f.getKomentars().add(k);
            k.setFestival(f);
            k.setId(new KomentarId(max+1, f.getIdFest()));
            session.saveOrUpdate(k);
            session.saveOrUpdate(f);
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void updateMedias(List<Media> slike, Festival festival) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            for(Media m : slike){
                festival.getMedias().add(m);
                m.setFestival(festival);
                Criteria c = session.createCriteria(Media.class,"m");
                c.add(Restrictions.eq("festival", festival));
                List<Media> list = (List<Media>)c.list();
                int max = 0;
                for(Media mm : list){
                    if(mm.getId().getIdmedia() > max) max = mm.getId().getIdmedia();
                }
                m.setId(new MediaId(festival.getIdFest(), max+1));
                session.saveOrUpdate(m);
            }
            session.saveOrUpdate(festival);
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }        
    }

    public boolean didUserBuyTicket(Korisnik k, Festival fest) {
        boolean ret = false;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Criteria c = session.createCriteria(Rezervacija.class, "rezervacija");
            c.add(Restrictions.eq("status", 1));
            c.add(Restrictions.eq("korisnik", k));
            c.add(Restrictions.in("dan", fest.getDans()));
            List<Rezervacija> rez = c.list();
            if(!rez.isEmpty()) ret = true;
            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }     
        return ret;
    }
    
}
