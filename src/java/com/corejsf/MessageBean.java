/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Poruka;
import db.helpers.KorisnikHelper;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author John
 */
@Named(value = "messageBean")
@SessionScoped
public class MessageBean implements Serializable {
    private KorisnikHelper helper = new KorisnikHelper();
    
    
    @Inject
    private LoginBean loginBean;
    
    public void showMessages() throws IOException{
        if(!loginBean.isUnregistered()){
            List<Poruka> messages = helper.getMessagesForUser(loginBean.getKorisnik());
            FacesContext context = FacesContext.getCurrentInstance();
            for(Poruka p : messages){
                context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Poruka", p.getDatum() + " - " + p.getSadrzaj()));
            }
            helper.removeMessages(messages);
            if(loginBean.getKorisnik().getBan() == 1){
                loginBean.logout();
            }
        }
    }

    

    
}
