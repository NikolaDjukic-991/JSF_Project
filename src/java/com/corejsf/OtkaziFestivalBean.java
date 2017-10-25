/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.mchange.v2.csv.FastCsvUtils;
import db.Festival;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.util.CSVBuilder;

/**
 *
 * @author John
 */
@Named(value = "otkaziFestivalBean")
@ViewScoped
public class OtkaziFestivalBean implements Serializable{
    private static FestivalHelper helper = new FestivalHelper();
    
    private String ime;
    private List<Festival> svifestivali;
    private List<Festival> festivali;
    
    
    @PostConstruct
    public void init(){
        svifestivali = helper.getUpcomingFestivals();
        festivali = new ArrayList<>(svifestivali);
    }
    
    
    public void filtrirajFestivale(){
//        svifestivali = helper.getUpcomingFestivals();
        festivali = new ArrayList<>(svifestivali);
        festivali.removeIf((Festival f)-> {
            return !f.getIme().toLowerCase().contains(ime.toLowerCase());
        });
    }
    
    public void otkazi(Festival festival){
        helper.cancelFestival(festival);
        filtrirajFestivale();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno otkazan festival", "Poslata poruka svim korisnicima"));
        
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public List<Festival> getSvifestivali() {
        return svifestivali;
    }

    public List<Festival> getFestivali() {
        return festivali;
    }
    
    
    
}
