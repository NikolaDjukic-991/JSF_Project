/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author John
 */
class TelefonConstraintValidator implements ConstraintValidator<Telefon, String> {
Pattern pattern;

private static final String TELEFON_PATTERN = "^(\\+\\d{3}(\\s)?\\d{2})|(\\d{3})/(\\d+)(-\\d+)*";
     
    @Override
    public void initialize(Telefon a) {
        pattern = Pattern.compile(TELEFON_PATTERN);
    }
 
    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {
        return pattern.matcher(value.toString()).matches();
    }
}
