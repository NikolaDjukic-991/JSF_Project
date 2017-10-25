/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import db.Festival;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author John
 */
@Named(value = "dialogSupportBean")
@SessionScoped
public class DialogSupportBean implements Serializable {

    private Festival fest;
    
    
    public void initialize(Festival fest){
        this.fest = fest;
    }
    
    public void destroy(){
        this.fest = null;
    }
    

    public Festival getFest() {
        return fest;
    }

    public void setFest(Festival fest) {
        this.fest = fest;
    }
    
    
    
}
