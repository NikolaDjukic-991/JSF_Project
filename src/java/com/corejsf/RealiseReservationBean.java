/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.RezervacijaDisplay;
import db.Dan;
import db.Festival;
import db.Rezervacija;
import db.helpers.RezervacijaHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author John
 */
@Named(value = "realiseReservationBean")
@ViewScoped
public class RealiseReservationBean implements Serializable {
    private static RezervacijaHelper helper = new RezervacijaHelper();
    
    private String uname = "";
    private String imefestivala = "";
    private Integer resId;
    private List<RezervacijaDisplay> sverezervacije = new ArrayList<>();
    private List<RezervacijaDisplay> rezervacije = new ArrayList<>();
    
    public void showReservationsForUser(){
        sverezervacije.clear();
        List<Rezervacija> rez_raw;
        rez_raw = helper.getReservacijeForUser(uname);
        for(Rezervacija r : rez_raw){
            sverezervacije.add(new RezervacijaDisplay(r));
        }
        rezervacije.addAll(sverezervacije);
    }
    
    public void realizujRezervaciju(RezervacijaDisplay rez){
        FacesContext context = FacesContext.getCurrentInstance();
        Rezervacija r = helper.getRezervacijaById(rez.getIdRez());
        if(helper.successfulReservation(r.getIdRez())){
            if( rez.getTip() == 0 ) {
                helper.updateDans(r);
            }
            if( rez.getTip() == 1 ){
                helper.updateDan(r);
            }
            
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", "Rezervacija uspesno realizovana."));
        } else {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Greska u bazi."));
        }
    }
    
    public void filtirirajPoFestivalu(){
        if(imefestivala.equals("")){
            rezervacije.clear();
            rezervacije.addAll(sverezervacije);
        } else {
            rezervacije.clear();
            rezervacije.addAll(sverezervacije);
            rezervacije.removeIf((RezervacijaDisplay rd) -> {
                return !rd.getFestival().getIme().toLowerCase().contains(imefestivala.toLowerCase());
            });
        }
    }
    
    public void filtrirajPoIdRezervacije(Integer idrez){
        if(idrez == null){
            rezervacije.clear();
            rezervacije.addAll(sverezervacije);
        } else {
            rezervacije.removeIf((RezervacijaDisplay rd) -> {
                return !(rd.getIdRez() == idrez);
            });
        }
        
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public List<RezervacijaDisplay> getRezervacije() {
        return rezervacije;
    }

    public String getImefestivala() {
        return imefestivala;
    }

    public void setImefestivala(String imefestivala) {
        this.imefestivala = imefestivala;
    }
    
    
        
}
