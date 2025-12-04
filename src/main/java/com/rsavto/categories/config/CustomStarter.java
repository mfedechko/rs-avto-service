package com.rsavto.categories.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfedechko
 */
@Component
public class CustomStarter implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    @Scheduled
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("Application started!");
    }



}

