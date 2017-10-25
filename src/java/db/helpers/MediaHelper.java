/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import db.Festival;
import db.Media;
import db.MediaId;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class MediaHelper {
    private Session session = null;
    
    public MediaHelper(){
    }
    
    public String getPicturePathForFest(Festival f){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        String picPath = null;
        
        ArrayList<Integer> idList = new ArrayList<>();
        for(Festival sisterFest : f.getFestivalgrupa().getFestivals()){
            idList.add(sisterFest.getIdFest());
        }
        
        org.hibernate.Transaction tx = session.beginTransaction();
        try{         
            Criteria c = session.createCriteria(Media.class);
            c.setMaxResults(1);
            c.add(Restrictions.in("id.idFest", idList));
            c.add(Restrictions.eq("tip", 3));
            List<Media> list = c.list();
            Media m = (Media) list.get(0);
            picPath = m.getPath();
            tx.commit();
        } catch (Exception e){
            picPath = "D:\\Nikola\\NetBeansProjects\\pia_add\\logo.png";
            e.printStackTrace();
            tx.rollback();
        }
        return picPath;
    }
    
    public void saveMedia(Media m){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{    
            int max = -1;
            for(Media mm : m.getFestival().getMedias()){
                if(mm.getId().getIdmedia() > max) max = mm.getId().getIdmedia();
            }
            m.setId(new MediaId(m.getFestival().getIdFest(), max+1));
            session.saveOrUpdate(m);
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void allowAll() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try{    
            Criteria c = session.createCriteria(Media.class);
            c.add(Restrictions.eq("odobrena", 0));
            List<Media> list = (List<Media>) c.list();
            for(Media m : list){
                m.setOdobrena(1);
                session.saveOrUpdate(m);
            }
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
    }
    
}
