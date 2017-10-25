/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.validation.Lozinka;
import db.Korisnik;
import db.helpers.KorisnikHelper;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author John
 */
@Named(value = "changePassBean")
@ViewScoped
public class ChangePassBean implements Serializable {
    private static KorisnikHelper helper = new KorisnikHelper();
    
    @Inject
    private LoginBean loginBean;
    
    private String oldPass ="";
    @Lozinka
    private String newPass ="";

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
    
    public void changePass() throws IOException{
        Korisnik k = loginBean.getKorisnik();
        k.setLozinka(newPass);
        
        helper.updateKorisnik(k);
        loginBean.logout();
        
    }
    
    
    
    
}
