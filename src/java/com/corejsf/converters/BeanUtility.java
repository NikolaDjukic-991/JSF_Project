/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf.converters;

import java.util.Collections;
import java.util.Set;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 *
 * @author John
 */
public class BeanUtility {

    public static <T> T getReference(BeanManager beanManager, Class<T> beanClass) {
        Bean<T> bean = resolve(beanManager, beanClass);
        return (bean != null) ? getReference(beanManager, bean) : null;
    }

    public static <T> Bean<T> resolve(BeanManager beanManager, Class<T> beanClass) {
        Set<Bean<?>> beans = beanManager.getBeans(beanClass);

        for (Bean<?> bean : beans) {
            if (bean.getBeanClass() == beanClass) {
                return (Bean<T>) beanManager.resolve(Collections.<Bean<?>>singleton(bean));
            }
        }

        return (Bean<T>) beanManager.resolve(beans);
    }

    public static <T> T getReference(BeanManager beanManager, Bean<T> bean) {
        return (T) beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
    }
}
