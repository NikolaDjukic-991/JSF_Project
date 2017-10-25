/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Korisnik;
import db.helpers.KorisnikHelper;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author John
 */
@Named(value = "odobriKorisnikeBean")
@ViewScoped
public class OdobriKorisnikeBean implements Serializable {
    private static KorisnikHelper helper = new KorisnikHelper();
    private List<Korisnik> korisnici;
    /**
     * Creates a new instance of OdobriKorisnikeBean
     */
    public OdobriKorisnikeBean() {
    }
    
    @PostConstruct
    public void init(){
        korisnici = helper.getKorisniciWithType(2);
    }
    
    public void odobri(Korisnik k){
        FacesContext context = FacesContext.getCurrentInstance();
        k.setTip(0);
        helper.updateKorisnik(k);
        context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno odobravanje", "Korisnik je uspesno odobren."));
        korisnici.remove(k);
    }
    
    public void odbij(Korisnik k){
        FacesContext context = FacesContext.getCurrentInstance();
        helper.deleteKorisnik(k);
        context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno odbijanje", "Korisnik je izbrisan iz sistema."));
        korisnici.remove(k);
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }
    
    
    
}
