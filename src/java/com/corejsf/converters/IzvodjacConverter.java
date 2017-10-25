/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.converters;

import com.corejsf.IzvodjaciService;
import db.Izvodjac;
import db.Nastupa;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author John
 */
@FacesConverter("izvodjacConverter")
public class IzvodjacConverter implements Converter{
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
                IzvodjaciService service = BeanUtility.getReference(beanManager, IzvodjaciService.class);
                return service.getIzvodjaci().get(Integer.parseInt(value)-1);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greska prilikom konverzije", "Nije validan izvodjac."));
            } catch (NamingException ex) {
                Logger.getLogger(IzvodjacConverter.class.getName()).log(Level.SEVERE, null, ex);
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greska prilikom konverzije", "Converter error."));
            }
        }
        else {
            return null;
        }
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Izvodjac) object).getIdIzvodjac());
        }
        else {
            return null;
        }
    } 
}
