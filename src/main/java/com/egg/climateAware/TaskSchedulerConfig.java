/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware;

import com.egg.climateAware.servicios.CampanaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TaskSchedulerConfig {

    @Autowired
    private CampanaServicio campanaServicio;

    @Scheduled(cron = "0 * * * * *") // Ejecutar todos los días a la medianoche
    public void darDeBajaCampanasAntiguas() {
        campanaServicio.darDeBajaCampanasAntiguas();
    }
}