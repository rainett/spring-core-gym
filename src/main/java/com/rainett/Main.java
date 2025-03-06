package com.rainett;

import com.rainett.service.AnomalyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final String BASE_PACKAGE = "com.rainett";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BASE_PACKAGE);
        AnomalyService anomalyService = context.getBean(AnomalyService.class);
        System.out.println(anomalyService.getAnomaly());
    }
}