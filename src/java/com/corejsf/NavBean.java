package com.corejsf;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author John
 */

@SessionScoped
@Named("navBean")
public class NavBean implements Serializable {
    
    private String previousPage = "";
    private String activePage = "login";
    private String topPage = "default";
    private String leftPage = "genericleft";
    
    @Inject
    private LoginBean loginbean;
    
    
    public void showRegisterPage() {
        previousPage = activePage;
        activePage = "register";
    }

    public String getActivePage() {
        return activePage;
    }

    public void setActivePage(String activePage) {
        previousPage = this.activePage;
        this.activePage = activePage;
    }
    
    
    
    public void back(){
        activePage = previousPage;
        previousPage = "";
    }

    public String getTopPage() {
        return topPage;
    }

    public void setTopPage(String topPage) {
        this.topPage = topPage;
    }
    
    public void home(){
        if(loginbean.getKorisnik() != null)
            if(loginbean.getKorisnik().getTip() == 1){
                setActivePage("homeadmin");
            } else {
                setActivePage("homekorisnik");
            }
        else {
            setActivePage("homekorisnik");
        }
    }

    public void setLeftPage(String leftPage) {
        this.leftPage = leftPage;
    }
    
    public void showAddFestival(){
        setActivePage("dodajfestival");
    }

    public String getLeftPage() {
        return leftPage;
    }
    
    public void showUserHome(){
        setActivePage("homekorisnik");
    }
    
    public void showLoginPage(){
        setActivePage("login");
    }
    
    public void showIzmeniFestival(){
        setActivePage("izmenifestival");
    }
    
    public void showOdobriKorisnika(){
        setActivePage("odobrikorisnika");
    }
    
    public void showRecentLogins(){
        setActivePage("recentlogins");
    }
    
    public void showImportFestival(){
        setActivePage("importfestival");
    }
    
    public void showOdobriProdaju(){
        setActivePage("odobriprodaju");
    }
    
    public void showProdajaNeregistrovanim(){
        setActivePage("prodajaneregistrovanim");
    }

    public void showDetailsPage() {
        previousPage = activePage;
        if(loginbean.isUnregistered()){
            setActivePage("login");
        } else {
            setActivePage("festivaldetails");
        }
    }
    
    public void showChangePass() {
        setActivePage("changepass");
    }
    
    public void showOtkaziFestival() {
        setActivePage("otkazifestival");
    }
    
    public void showDetailsPreviewPage() {
        setActivePage("festivalpreview");
    }

    
}
