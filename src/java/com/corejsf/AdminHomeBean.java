/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.FestivalDisplay;
import db.Festival;
import db.helpers.FestivalHelper;
import db.helpers.MediaHelper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author John
 */
@Named(value = "adminHomeBean")
@RequestScoped
public class AdminHomeBean {
    private static FestivalHelper helper = new FestivalHelper();
    private static MediaHelper mediahelper = new MediaHelper();

    private List<FestivalDisplay> mostViewed = new ArrayList<>();
    private List<FestivalDisplay> mostBought = new ArrayList<>();
    
    @PostConstruct
    public void init(){
        List<Festival> mostViewedFestival = helper.getMostViewed();
        List<Festival> mostBoughtFestival = helper.getMostBought();
        
        for(Festival f : mostViewedFestival){
            mostViewed.add(new FestivalDisplay(f));
        }
        
        for(Festival f : mostBoughtFestival){
            mostBought.add(new FestivalDisplay(f));
        }
    }
    
    public void odobri(){
        mediahelper.allowAll();
    }

    public List<FestivalDisplay> getMostViewed() {
        return mostViewed;
    }

    public List<FestivalDisplay> getMostBought() {
        return mostBought;
    }
    
}
