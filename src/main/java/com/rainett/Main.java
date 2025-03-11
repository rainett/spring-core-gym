package com.rainett;

import com.rainett.facade.TrainingFacade;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.service.TraineeService;
import com.rainett.service.TrainerService;
import com.rainett.service.TrainingService;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final String BASE_PACKAGE = "com.rainett";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BASE_PACKAGE);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);

        TraineeService traineeService = trainingFacade.getTraineeService();
        List<Trainee> trainees = traineeService.getAll();
        System.out.println("Trainees: " + trainees);

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeService.createProfile(trainee);

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("John");
        trainee2.setLastName("Doe");
        traineeService.createProfile(trainee2);

        trainees = traineeService.getAll();
        System.out.println("Trainees: " + trainees);

        TrainerService trainerService = trainingFacade.getTrainerService();
        List<Trainer> trainers = trainerService.getAll();
        System.out.println("Trainers: " + trainers);

        TrainingService trainingService = trainingFacade.getTrainingService();
        List<Training> trainings = trainingService.getAll();
        System.out.println("Trainings: " + trainings);
    }
}