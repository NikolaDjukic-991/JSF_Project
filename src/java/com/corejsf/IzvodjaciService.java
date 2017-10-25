/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Izvodjac;
import db.helpers.IzvodjacHelper;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author John
 */
@Named(value = "izvodjaciService")
@ApplicationScoped
public class IzvodjaciService {
    private static IzvodjacHelper helper = new IzvodjacHelper();
    
    private List<Izvodjac> izvodjaci;

    @PostConstruct
    public void init(){
        izvodjaci = helper.getIzvodjaci();
    }
    
    public void updateIzvodjaci(){
        izvodjaci = helper.getIzvodjaci();
    }

    public List<Izvodjac> getIzvodjaci() {
        return izvodjaci;
    }

    
    
    
}
