/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Korisnik;
import db.helpers.KorisnikHelper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author John
 */
@Named(value = "recentUsers")
@RequestScoped
public class RecentUsers {
    private static KorisnikHelper helper = new KorisnikHelper();
    private List<Korisnik> korisnici = Collections.EMPTY_LIST;
    
    
    /**
     * Creates a new instance of recentUsers
     */
    public RecentUsers() {
    }
    
    @PostConstruct
    public void init(){
        korisnici = helper.getRecent();
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }
    
    
}
