/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.display.FestivalDisplay;
import db.Festival;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author John
 */
@Named(value = "userHomeBean")
@SessionScoped
public class UserHomeBean implements Serializable {
    private static FestivalHelper helper = new FestivalHelper();
    private List<FestivalDisplay> recentOrTop;
    
    
    private String recentOrTopHeader;
    
    @Inject
    LoginBean loginBean;

    public UserHomeBean() {
        recentOrTop = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        boolean showingTop = loginBean.isUnregistered();
        List<Festival> festList = showingTop  ? helper.getTopRated() : helper.getMostRecent();
        recentOrTopHeader = showingTop ? "Najbolje ocenjeni festivali" : "Uskoro";
        for(Festival f : festList){
            recentOrTop.add(new FestivalDisplay(f));
        }
    }

    public List<FestivalDisplay> getRecentOrTop() {
        return recentOrTop;
    }

    public void setRecentOrTop(List<FestivalDisplay> recentOrTop) {
        this.recentOrTop = recentOrTop;
    }

    public String getRecentOrTopHeader() {
        return recentOrTopHeader;
    }

    public void setRecentOrTopHeader(String recentOrTopHeader) {
        this.recentOrTopHeader = recentOrTopHeader;
    }
    
    
    
    
}
