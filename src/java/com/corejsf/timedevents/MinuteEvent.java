/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.timedevents;

import db.Poruka;
import db.Rezervacija;
import db.helpers.FestivalHelper;
import db.helpers.KorisnikHelper;
import db.helpers.RezervacijaHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author John
 */
public class MinuteEvent extends TimerTask{
    private static RezervacijaHelper helper = new RezervacijaHelper();
    private static KorisnikHelper korisnikhelper = new KorisnikHelper();
    private static FestivalHelper festivalhelper = new FestivalHelper();
    
    private List<Poruka> poruke = new ArrayList<>();
    
    @Override
    public void run() {
        helper.updateIstekleRezervacije();
        List<Rezervacija> rezervacije = helper.getIstekleRezervacije();
        poruke.clear();
        for(Rezervacija r : rezervacije){
            String poruka = "Vasa rezervacija za "+r.getDan().getFestival().getIme()+" je istekla.";
            Poruka p = new Poruka(r.getKorisnik(), "", new Date());
            r.getKorisnik().setPrestupi(r.getKorisnik().getPrestupi()+1);
            int brPreostalihPrestupa = 3 - r.getKorisnik().getPrestupi();
            if(brPreostalihPrestupa <= 0){
                r.getKorisnik().setBan(1);
                poruka += " Nemate vise prava na prestupe, vas nalog je suspendovan";
            } else {
                poruka = " Imate pravo na jos "+brPreostalihPrestupa+" prestupa.";
            }
            p.setSadrzaj(poruka);
            r.setTip(3); // evidentiran istek.
            
            korisnikhelper.updateKorisnik(r.getKorisnik());
            korisnikhelper.addMessage(p);
            helper.updateRezervacija(r);
            festivalhelper.revertRezervacija(r);
        }
        
        
    }
    
}
