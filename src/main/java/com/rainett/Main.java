package com.rainett;

import com.rainett.facade.TrainingFacade;
import com.rainett.model.Trainee;
import com.rainett.service.TraineeService;
import com.rainett.storage.DataStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final String BASE_PACKAGE = "com.rainett";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BASE_PACKAGE);
        TrainingFacade facade = context.getBean(TrainingFacade.class);
        TraineeService traineeService = facade.getTraineeService();
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        Long userId = traineeService.createProfile(trainee);
        trainee.setUserId(userId);
        System.out.println("Created trainee: " + traineeService.getProfile(userId));
        trainee.setFirstName("Jane");
        traineeService.updateProfile(trainee);
        System.out.println("Updated trainee: " + traineeService.getProfile(userId));
        traineeService.deleteProfile(userId);
        System.out.println("Deleted trainee: " + traineeService.getProfile(userId));

        DataStorage dataStorage = context.getBean(DataStorage.class);
        System.out.println("Trainees: " + dataStorage.getNamespace("trainees"));
        System.out.println("Trainers: " + dataStorage.getNamespace("trainers"));
        System.out.println("Trainings: " + dataStorage.getNamespace("trainings"));
    }
}