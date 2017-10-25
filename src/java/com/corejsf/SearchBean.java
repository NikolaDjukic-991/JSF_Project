/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.FestivalDisplay;
import db.Festival;
import db.helpers.FestivalHelper;
import db.Izvodjac;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author John
 */
@Named(value = "searchBean")
@ViewScoped
public class SearchBean implements Serializable {
    private static FestivalHelper helper = new FestivalHelper();
    
    private List<Izvodjac> izabraniIzvodjaci = null;
    private Date datumOd = null;
    private Date datumDo = null;
    private String naziv = "";
    private String mesto = "";
    
    private Date datumSide = null;
    
    private Date danasnjiDatum = new Date();
    private Date datumOdIliDanas = danasnjiDatum;
    
    @Inject
    private IzvodjaciService izvodjaciService;
    
    
    @Inject
    private NavBean navbean;
    
    private List<FestivalDisplay> rezultati = null;
    
    private boolean imaRezultata = false;
    
    
    public List<Izvodjac> completeIzvodjaci(String query) {
        List<Izvodjac> sviIzvodjaci = izvodjaciService.getIzvodjaci();
        List<Izvodjac> filtriraniIzvodjaci = new ArrayList<>();
         
        for (int i = 0; i < sviIzvodjaci.size(); i++) {
            Izvodjac izvodjac = sviIzvodjaci.get(i);
            if(izvodjac.getIme().toLowerCase().startsWith(query.toLowerCase())) {
                filtriraniIzvodjaci.add(izvodjac);
            }
        }
         
        return filtriraniIzvodjaci;
    }
    
    public void izabranOd(){
        if(datumOd != null){
            datumOdIliDanas = datumOd;
            if(datumDo != null){
                if(datumOd.after(datumDo)) datumDo = null;
            }
        }
    }
    
    public void pretraga(){
        if(!(izabraniIzvodjaci != null && !izabraniIzvodjaci.isEmpty())
                && datumOd == null
                && datumDo == null
                && !(naziv != null && !naziv.isEmpty())
                && !(mesto != null && !mesto.isEmpty())){
            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage("Neuspesna pretraga", "Morate uneti bar jedan kriterijum pretrage"));
            imaRezultata = false;
            return;
        }
        
        List<Festival> search = helper.search(izabraniIzvodjaci, datumOd, datumDo, naziv, mesto);
        rezultati = new ArrayList<>();
        if(search != null && !search.isEmpty()){
            for(Festival f : search){
                rezultati.add(new FestivalDisplay(f));
            }
            imaRezultata = true;
        }
        else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage("Neuspesna pretraga", "Ne postoji nijedan festival koji odgovara vasoj pretrazi"));
            imaRezultata = false;
        }
    }
    
    public void izabranSide(){
        List<Festival> search = helper.getFestivalsOnDate(datumSide);
        rezultati = new ArrayList<>();
        if(search != null && !search.isEmpty()){
            for(Festival f : search){
                rezultati.add(new FestivalDisplay(f));
            }
            imaRezultata = true;
            
            izabraniIzvodjaci = null;
            datumOd = null;
            datumDo = null;
            naziv = "";
            mesto = "";
            
            navbean.setActivePage("homekorisnik");
        }
        else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("notificationbubble", new FacesMessage("Neuspesna pretraga", "Ne postoji nijedan festival koji odgovara vasoj pretrazi"));
            imaRezultata = false;
        }
    }
    
    public void izabranDo(){
    }

    public List<Izvodjac> getIzabraniIzvodjaci() {
        return izabraniIzvodjaci;
    }

    public void setIzabraniIzvodjaci(List<Izvodjac> izabraniIzvodjaci) {
        this.izabraniIzvodjaci = izabraniIzvodjaci;
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

    public Date getDanasnjiDatum() {
        return danasnjiDatum;
    }

    public void setDanasnjiDatum(Date danasnjiDatum) {
        this.danasnjiDatum = danasnjiDatum;
    }

    public Date getDatumOdIliDanas() {
        return datumOdIliDanas;
    }

    public void setDatumOdIliDanas(Date datumOdIliDanas) {
        this.datumOdIliDanas = datumOdIliDanas;
    }

    public List<FestivalDisplay> getRezultati() {
        return rezultati;
    }

    public void setRezultati(List<FestivalDisplay> rezultati) {
        this.rezultati = rezultati;
    }

    public boolean isImaRezultata() {
        return imaRezultata;
    }

    public void setImaRezultata(boolean imaRezultata) {
        this.imaRezultata = imaRezultata;
    }

    public Date getDatumSide() {
        return datumSide;
    }

    public void setDatumSide(Date datumSide) {
        this.datumSide = datumSide;
    }
    
    
    
    
    
}
