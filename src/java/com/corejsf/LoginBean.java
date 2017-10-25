/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Korisnik;
import db.helpers.KorisnikHelper;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author John
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static KorisnikHelper helper = new KorisnikHelper();;
    private String uname;
    private String lozinka;
    private String msg;
    
    private boolean unregistered = true;
    
    @Inject
    private NavBean navbean;
    
    
    private Korisnik korisnik;
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean(){
    }
    
    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        korisnik = helper.getKorisnik(uname, lozinka);
        if(korisnik == null){
            msg = "Neispravno korisnicko ime i/ili lozinka";
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Neuspesna prijava", msg));
        } else {
            
            if(korisnik.getBan() == 1){
                msg = "Vas nalog je suspendovan.";
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Suspendovan nalog", msg));
                return;
            }
            
            if(korisnik.getTip() == 0){
                msg = "Dobrodosli "+korisnik.getIme();
                helper.updateKorisnikTimeStamp(korisnik);
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO,"Uspesna prijava", msg));
                navbean.setTopPage("topkorisnik");
                navbean.setActivePage("homekorisnik");
                unregistered = false;
            }
            if(korisnik.getTip() == 1){
                msg = "Dobrodosli "+korisnik.getIme();
                helper.updateKorisnikTimeStamp(korisnik);
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesna prijava", msg));
                navbean.setActivePage("homeadmin");
                navbean.setLeftPage("adminleft");
                unregistered = false;
            }
            if(korisnik.getTip() == 2){
                msg = "Vas nalog nije odobren od strane administratora.";
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neuspesna prijava", msg));
                
            }
            
        }
    }
    
    public void logout() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
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

    public String getMsg() {
        return msg;
    }


    public Korisnik getKorisnik() {
        return korisnik;
    }

    public boolean isUnregistered() {
        return unregistered;
    }


    
    
    
}
