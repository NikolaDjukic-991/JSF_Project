/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.display;

import db.Festival;
import db.Media;
import db.helpers.MediaHelper;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author John
 */
public class FestivalDisplay implements Serializable {
    private static MediaHelper helper = new MediaHelper();
    
    private Integer idFest;
    private String ime;
    private String mesto;
    private Date od;
    private Date do_;
    private String slika;
    
    
        
    public FestivalDisplay(Festival f){
        idFest = f.getIdFest();
        ime = f.getIme();
        mesto = f.getMesto();
        od = f.getOd();
        do_ = f.getDo_();
       
        slika = helper.getPicturePathForFest(f);
        
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Date getOd() {
        return od;
    }

    public void setOd(Date od) {
        this.od = od;
    }

    public Date getDo_() {
        return do_;
    }

    public void setDo_(Date do_) {
        this.do_ = do_;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public Integer getIdFest() {
        return idFest;
    }

    public void setIdFest(Integer idFest) {
        this.idFest = idFest;
    }
    
    
}
