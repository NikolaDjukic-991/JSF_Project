/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.display;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class Satnica {
    private List<Nastup> nastupi;
    
    public Satnica(){
        nastupi = new ArrayList();
    }
    
    public void dodajNastup(Nastup n){
        nastupi.add(n);
    }

    public List<Nastup> getNastupi() {
        return nastupi;
    }
    
    
    
    
}
