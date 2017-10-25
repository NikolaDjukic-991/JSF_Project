/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validation;

import db.helpers.KorisnikHelper;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author John
 */
class UnameConstraintValidator implements ConstraintValidator<Uname, String> {

    private static KorisnikHelper helper = new KorisnikHelper();
    
    @Override
    public void initialize(Uname a) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return false;

        return helper.getKorisnik(value) == null;
    }
    
}
