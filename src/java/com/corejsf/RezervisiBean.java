/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Dan;
import db.Festival;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author John
 */
@Named(value = "rezervisiBean")
@ViewScoped
public class RezervisiBean implements Serializable{

    private static FestivalHelper helper  = new FestivalHelper();
    
    private Festival fest;
    private String tiprez = "paket";
    private int brKarata = 1;
    private int cena;
    private Integer izabranDan = null;
    private List<Dan> dans;
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private DialogSupportBean dialogSupportBean;
    
    
    @PostConstruct
    public void init(){
        
        fest = dialogSupportBean.getFest();
        cena = fest.getCenaPaket();
        
        dans = new ArrayList<>(fest.getDans());
        dans.sort((Dan d1, Dan d2) -> {
            return d1.getId().getIdDan() - d2.getId().getIdDan();
        });
        
        dialogSupportBean.destroy();
    }
    
    /**
     * Creates a new instance of RezervisiBean
     */

    
    public void rezervisi(){
        String ret = "";
        if(tiprez.equals("paket")){
            if(helper.rezervisiPaket(fest, loginBean.getKorisnik(), brKarata) == 0){
                ret = "paketsuccess";
            } else {
                ret = "paketfailiure";
            }
        }
        if(tiprez.equals("dan")){
            Dan izabran = null;
            for(Dan d : fest.getDans()){
                if(d.getId().getIdDan() == izabranDan){
                    izabran = d;
                }
            }
            if(helper.rezervisiDan(izabran, loginBean.getKorisnik(), brKarata) == 0){
                ret = "dansuccess";
            } else {
                ret = "danfailiure";
            }
        }
        RequestContext.getCurrentInstance().closeDialog(ret);
    }
    
    public void brKarataChange(){
        if(tiprez.equals("paket")){
            cena = brKarata * fest.getCenaPaket();
        }
        
        if(tiprez.equals("dan")){
            cena = brKarata * fest.getCenaDan();
        }
    }

    public Festival getFest() {
        return fest;
    }

    public void setFest(Festival fest) {
        this.fest = fest;
    }

    public String getTiprez() {
        return tiprez;
    }

    public void setTiprez(String tiprez) {
        this.tiprez = tiprez;
    }

    public int getBrKarata() {
        return brKarata;
    }

    public void setBrKarata(int brKarata) {
        this.brKarata = brKarata;
    }

    public Integer getIzabranDan() {
        return izabranDan;
    }

    public void setIzabranDan(Integer izabranDan) {
        this.izabranDan = izabranDan;
    }

    public int getCena() {
        return cena;
    }

    public List<Dan> getDans() {
        return dans;
    }
    
    
    
    
}
