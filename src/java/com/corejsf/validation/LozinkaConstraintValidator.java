/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author John
 */
class LozinkaConstraintValidator implements ConstraintValidator<Lozinka, String> {

    
    @Override
    public void initialize(Lozinka a) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.length() > 7;
    }
    
}
