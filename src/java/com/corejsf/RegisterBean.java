/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.validation.Email;
import com.corejsf.validation.Lozinka;
import com.corejsf.validation.Uname;
import db.Korisnik;
import db.helpers.KorisnikHelper;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


/**
 *
 * @author John
 */
@RequestScoped
@Named("registerBean")
public class RegisterBean {
    
    @Uname
    private String uname;
    @Lozinka
    private String lozinka;
    private String lozinka2;
    private String ime;
    private String prezime;
    
    @Email
    private String email;
    
    
    private String telefon;
    private String msg;
    
    private static KorisnikHelper helper = new KorisnikHelper();
    
    @Inject
    private NavBean navbean;
    

    
    /**
     * Creates a new instance of RegisterBean
     */
    public void registerUser(){
        // TODO check shit
        FacesContext context = FacesContext.getCurrentInstance();
        Korisnik k = new Korisnik(uname, lozinka, ime, prezime, email, telefon, 0, 2, 0, null);
        if(helper.addKorisnik(k)) {
            msg = "Uspesna registracija, molimo vas sacekajte da administrator odobri vas nalog.";
            context.addMessage("notificationbubble", new FacesMessage("Nalog napravljen", msg));
            navbean.setActivePage("login");
        } else {
            msg = "Nalog sa korisnickim imenom ili emailom vec postoji.";
            context.addMessage("notificationbubble", new FacesMessage("Greska", msg));
        }
        
        
        
    }
    
    public void validate(){
        
    }
    

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getLozinka2() {
        return lozinka2;
    }

    public void setLozinka2(String lozinka2) {
        this.lozinka2 = lozinka2;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public KorisnikHelper getHelper() {
        return helper;
    }

    public void setHelper(KorisnikHelper helper) {
        this.helper = helper;
    }

    public NavBean getNavbean() {
        return navbean;
    }

    public void setNavbean(NavBean navbean) {
        this.navbean = navbean;
    }

    
}
