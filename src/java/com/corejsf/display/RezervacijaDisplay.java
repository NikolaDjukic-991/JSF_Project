/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.display;

import db.Festival;
import db.Rezervacija;
import db.helpers.FestivalHelper;
import java.util.Date;

/**
 *
 * @author John
 */
public class RezervacijaDisplay {

    private  Integer idRez;
    private  Date datum;
    private  int dan;
    private  Festival festival;
    private  int tip;
    private  String status;
    private  int brkarata;

    public RezervacijaDisplay(Rezervacija r) {
        idRez = r.getIdRez();
        datum = r.getDatum();
        dan = r.getDan().getId().getIdDan()+1;
        festival = r.getDan().getFestival();
        tip = r.getTip();
        
        if(r.getStatus() == 0){
            status = "Nerealizovana";
        }
        if(r.getStatus() == 1){
            status = "Realizovana";
        }
        if(r.getStatus() == 2){
            status = "Istekla";
        }
        if(r.getStatus() == 3){
            status = "Otkazan festival";
        }
        
        brkarata = r.getBrojkarata();
    }
    


    public Integer getIdRez() {
        return idRez;
    }

    public Date getDatum() {
        return datum;
    }

    public int getDan() {
        return dan;
    }

    public Festival getFestival() {
        return festival;
    }

    public int getTip() {
        return tip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBrkarata() {
        return brkarata;
    }
    
    
    
}
