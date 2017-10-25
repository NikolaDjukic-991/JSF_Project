/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.Nastup;
import com.corejsf.display.Satnica;
import db.Dan;
import db.Festival;
import db.Komentar;
import db.Korisnik;
import db.Media;
import db.Nastupa;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
/**
 *
 * @author John
 */
@Named(value = "festivalDetailsBean")
@SessionScoped
public class FestivalDetailsBean implements Serializable{
    private static FestivalHelper helper = new FestivalHelper();
    private Festival fest;
    private List<Media> slike;
    private List<Media> video;
    private List<Komentar> komentars;
    private List<Dan> dans = Collections.EMPTY_LIST;
    private List<Satnica> satnica = Collections.EMPTY_LIST;
    
    private int rating;
    private int cena;

    
    @Inject
    NavBean navbean;
    
    @Inject
    LoginBean loginBean;
    
    @Inject
    DialogSupportBean dialogSupport;
    
    /**
     * Creates a new instance of festivalDetailsBean
     */
    @PostConstruct
    public void init(){
        slike = new ArrayList<>();
        video = new ArrayList<>();
        komentars = new ArrayList<>();
    }
    
    public void rezervisiDijalog(){
        dialogSupport.initialize(fest);
        
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
        
        RequestContext.getCurrentInstance().openDialog("rezervisi", options, null);
    }
    
    public void back(){
        video.clear();
        slike.clear();
        satnica.clear();
        komentars.clear();
        dans.clear();
        navbean.back();
    }
    
    public void show(int idFest){
        
        FacesContext context = FacesContext.getCurrentInstance();
        fest = helper.getFestivalById(idFest);
        if(fest == null){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Festival ne postoji."));
        } else {
            navbean.showDetailsPage();
            
            dans = new ArrayList<>(fest.getDans());
            
            dans.sort((Dan d1, Dan d2)-> {
                return d1.getId().getIdDan() - d2.getId().getIdDan();
            });
            
            satnica = new ArrayList<>();
            for(Dan d : dans){
                Satnica s = new Satnica();
                ArrayList<Nastupa> nastupas = new ArrayList<>(d.getNastupas());
                nastupas.sort((Nastupa n1, Nastupa n2) -> {
                    return (int)(n1.getPocinje().getTime() - n2.getPocinje().getTime());
                });
                for(Nastupa n : nastupas){
                    String imeIzvodjaca = n.getIzvodjac().getIme();
                    Date od = n.getPocinje();
                    Date do_ = n.getZavrsava();
                    s.dodajNastup(new Nastup(od, do_, imeIzvodjaca));
                }
                
                satnica.add(s);
            }
            
            cena = fest.getCenaPaket();
            
            Set<Media> media;
            double rating = 0;
            int num = 0;
            
            Set<Komentar> festkom = fest.getKomentars();
            for(Festival f : fest.getFestivalgrupa().getFestivals()){
                media = f.getMedias();
                for(Media m : media){
                    if(m.getOdobrena() == 1){
                        if(m.getTip() == 1 || m.getTip() == 3 ){
                            slike.add(m);
                        }
                        if(m.getTip() == 2 || m.getTip() == 4){
                            video.add(m);
                        }
                    }
                }
                
                festkom = f.getKomentars();
                for(Komentar k : festkom){
                    komentars.add(k);
                    num++;
                    rating += k.getOcena();
                }
            }
            
            komentars.sort((Komentar k1, Komentar k2) -> {
                return (int) (k2.getDatum().getTime() - k1.getDatum().getTime());
            });
            
            this.rating = (int) Math.round(rating/num);
            helper.updateBrojacPregleda(fest);
            
        }
    }
    
    public void dialogRet(SelectEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        String ret = (String) event.getObject();
        if(ret.equals("paketsuccess")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", "Uspesno ste rezervisali kartu za festival."));
        }
        if(ret.equals("dansuccess")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", "Uspesno ste rezervisali kartu za dan."));

        }
        if(ret.equals("paketfailiure")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Greska"));
        }
        if(ret.equals("danfailiure")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Greska"));
        }
    }
    
    public boolean allowKomentar(){
        Korisnik k = loginBean.getKorisnik();
        boolean boughtTicket = helper.didUserBuyTicket(k,fest);
        return boughtTicket;
        
    }
    
    public void refreshOcena(){
        int n = 0;
        int zbir =0;
        for(Komentar k : fest.getKomentars()){
            if(k.getOcena() != null){
                n++;
                zbir += k.getOcena();
            }
            
        }
        if(n!=0)
            rating = (int) Math.round(zbir*1.0/n);
    }

    public Festival getFest() {
        return fest;
    }

    public List<Media> getSlike() {
        return slike;
    }

    public List<Media> getVideo() {
        return video;
    }

    public List<Komentar> getKomentars() {
        return komentars;
    }

    public int getRating() {
        return rating;
    }

    public List<Dan> getDans() {
        return dans;
    }

    public int getCena() {
        return cena;
    }

    public List<Satnica> getSatnica() {
        return satnica;
    }

}
