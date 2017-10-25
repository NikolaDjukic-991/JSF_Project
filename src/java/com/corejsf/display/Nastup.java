/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author John
 */
public class Nastup {
    private final String od;
    private final String do_;
    private final String izvodjac;
    
    private static final DateFormat formatter = new SimpleDateFormat("HH:mm");
    
    public Nastup(Date od, Date do_, String izvodjac){
        this.izvodjac = izvodjac;
        
        this.od = formatter.format(od);
        this.do_ = formatter.format(do_);
        
    }

    public String getOd() {
        return od;
    }

    public String getDo_() {
        return do_;
    }

    public String getIzvodjac() {
        return izvodjac;
    }
    
    
    
}
