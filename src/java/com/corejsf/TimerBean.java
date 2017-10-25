/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corejsf;

import com.corejsf.timedevents.MinuteEvent;
import java.util.Timer;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Named;

/**
 *
 * @author John
 */
@Named(value = "timerBean")
@ApplicationScoped
public class TimerBean {

    Timer timer;
    MinuteEvent event;
    
    
    /**
     * Creates a new instance of TimerBean
     */
    public TimerBean() {
    }
    
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init){
        event = new MinuteEvent();
        timer = new Timer("PerMinuteRefresh", true);
        timer.scheduleAtFixedRate(event, 0, 60*1000);
    }
    
    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        timer.cancel();
    }
    
}
