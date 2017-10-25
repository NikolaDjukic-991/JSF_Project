/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.primefaces.validate.bean.ClientConstraint;

/**
 *
 * @author John
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LozinkaConstraintValidator.class)
@ClientConstraint(resolvedBy = LozinkaClientValidationConstraint.class)
public @interface Lozinka {

    String message() default "Lozinka mora sadrzati najmanje 8 karaktera.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
