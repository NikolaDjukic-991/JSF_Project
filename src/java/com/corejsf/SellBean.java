/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.FestivalDisplay;
import db.Dan;
import db.Festival;
import db.Rezervacija;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Named(value = "sellBean")
@ViewScoped
public class SellBean implements Serializable {
    private static FestivalHelper helper = new FestivalHelper();
    
    private String naziv = "";
    private String mesto = "";
    private Date datumOd;
    private Date datumDo;
    private Date datumOdIliDanas = new Date();
    private boolean imaRezultata = false;
    private List<Festival> festivali;
    
    private Festival izabranFestival;
    private Dan izabranDan;
    
    private int remaining=0;
    private int brKarata = 1;
    private List<Dan> dani;
    
    private String tipkarte = "paket";
    
    @Inject
    private DialogSupportBean dialogSupport;
    
    public void izabranOd(){
        if(datumOd != null){
            datumOdIliDanas = datumOd;
            if(datumDo != null){
                if(datumOd.after(datumDo)) datumDo = null;
            }
        }
    }
    
    public void izabranDo(){
    }
    
    public void pretraga(){
        if(     datumOd == null
                && datumDo == null
                && !(naziv != null && !naziv.isEmpty())
                && !(mesto != null && !mesto.isEmpty())){
            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage("Neuspesna pretraga", "Morate uneti bar jedan kriterijum pretrage"));
            imaRezultata = false;
            return;
        }
        
        festivali = helper.search(null, datumOd, datumDo, naziv, mesto);
        if(festivali.isEmpty()){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage("Neuspesna pretraga", "Ne postoji nijedan festival koji odgovara vasoj pretrazi"));
            imaRezultata = false;
        } else {
            imaRezultata = true;
        }
    }
    
    public void radioSelect(Festival f){
        izabranFestival = f;
        dani = new ArrayList<>(f.getDans());
        dani.sort((Dan d1, Dan d2) -> {
            return d1.getId().getIdDan() - d2.getId().getIdDan();
        });
    }
    
    public void danSelected(Dan d){
        izabranDan = d;
        remaining = d.getBrKarata() - (d.getBrProdatihKarata() + d.getBrRezervisanihKarata());
    }
   
    
    public void prodajDijalog(Festival fest){
        dialogSupport.initialize(fest);
        
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
        
        RequestContext.getCurrentInstance().openDialog("neregistrovanikupidijalog", options, null);        
    }
    
    public void dialogRet(SelectEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        String ret = (String) event.getObject();
        if(ret.equals("paketsuccess")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", "Uspesno ste prodali kartu za festival."));
        }
        if(ret.equals("dansuccess")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspeh!", "Uspesno ste prodali kartu za dan."));

        }
        if(ret.equals("paketfailiure")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Greska"));
        }
        if(ret.equals("danfailiure")){
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Greska!", "Greska"));
        }
    }
    
    
    private void reset(){
        brKarata = 1;
        dani = Collections.EMPTY_LIST;
        datumDo = null;
        datumDo = null;
        festivali = Collections.EMPTY_LIST;
        imaRezultata = false;
        izabranDan = null;
        izabranFestival = null;
        mesto = "";
        naziv = "";
        remaining = 0;
        tipkarte = "paket";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Date getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(Date datumOd) {
        this.datumOd = datumOd;
    }

    public Date getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(Date datumDo) {
        this.datumDo = datumDo;
    }

    public Date getDatumOdIliDanas() {
        return datumOdIliDanas;
    }

    public boolean isImaRezultata() {
        return imaRezultata;
    }

    public List<Festival> getFestivali() {
        return festivali;
    }

    public Festival getIzabranFestival() {
        return izabranFestival;
    }

    public void setIzabranFestival(Festival izabranFestival) {
        this.izabranFestival = izabranFestival;
    }

    public Dan getIzabranDan() {
        return izabranDan;
    }

    public void setIzabranDan(Dan izabranDan) {
        this.izabranDan = izabranDan;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getBrKarata() {
        return brKarata;
    }

    public void setBrKarata(int brKarata) {
        this.brKarata = brKarata;
    }

    public String getTipkarte() {
        return tipkarte;
    }

    public void setTipkarte(String tipkarte) {
        this.tipkarte = tipkarte;
    }

    public List<Dan> getDani() {
        return dani;
    }
    
    
}
