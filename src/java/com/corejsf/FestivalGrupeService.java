/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.timedevents.MinuteEvent;
import db.helpers.FestivalGrupeHelper;
import db.Festivalgrupa;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

/**
 *
 * @author John
 */
@Named(value = "festivalGrupeService")
@ApplicationScoped
public class FestivalGrupeService {

    private static FestivalGrupeHelper helper = new FestivalGrupeHelper();
    
    private List<Festivalgrupa> svegrupe = new ArrayList<>();
    /**
     * Creates a new instance of FestivalGrupeService
     */
    public FestivalGrupeService() {
    }
    
    
    @PostConstruct
    public void init(){
        svegrupe = helper.getFestivalGrupe();
    }
    
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init){
        svegrupe = helper.getFestivalGrupe();
    }
    
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
    }

    public List<Festivalgrupa> getSveGrupe() {
        return svegrupe;
    }
    
    private void updateGrupe(){
        svegrupe = getSveGrupe();
    }
    
}
