package com.rainett;

import com.rainett.dto.trainee.CreateTraineeProfileRequest;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.trainer.CreateTrainerProfileRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.dto.user.UsernameRequest;
import com.rainett.facade.TrainingFacade;
import com.rainett.model.Trainee;
import com.rainett.service.TraineeService;
import com.rainett.service.TrainerService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final String BASE_PACKAGE = "com.rainett";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BASE_PACKAGE);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);

        TraineeService traineeService = trainingFacade.getTraineeService();
        CreateTraineeProfileRequest request = new CreateTraineeProfileRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAddress("123 Main St");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Trainee traineeProfile = traineeService.createProfile(request);
        traineeService.createProfile(request);
        traineeService.createProfile(request);

        TrainerService trainerService = trainingFacade.getTrainerService();
        CreateTrainerProfileRequest request1 = new CreateTrainerProfileRequest();
        request1.setFirstName("Jane");
        request1.setLastName("Doe");
        request1.setSpecialization("Boxing");
        trainerService.createProfile(request1);
        request1.setSpecialization("Yoga");
        trainerService.createProfile(request1);

        UsernameRequest request2 = new UsernameRequest();
        request2.setIdentity(traineeProfile.getUsername());
        request2.setPassword(traineeProfile.getPassword());
        String username = "John.Doe.2";
        request2.setUsername(username);
        Trainee trainee = traineeService.findByUsername(request2);
        System.out.println("\nTrainee " + username + " found: " + trainee + "\n");

        UpdatePasswordRequest request3 = new UpdatePasswordRequest();
        request3.setIdentity(traineeProfile.getUsername());
        request3.setPassword(traineeProfile.getPassword());
        request3.setUsername(username);
        request3.setNewPassword("newPassword");
        trainee = traineeService.updatePassword(request3);
        System.out.println("\nTrainee " + username + " updated password: " + trainee + "\n");

        UpdateTraineeRequest request4 = new UpdateTraineeRequest();
        request4.setIdentity(traineeProfile.getUsername());
        request4.setPassword(traineeProfile.getPassword());
        request4.setUsername(username);
        request4.setFirstName("Jonathan");
        request4.setLastName("Doe");
        request4.setAddress("123 Main St");
        request4.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee = traineeService.updateTrainee(request4);
        username = trainee.getUsername();
        System.out.println("\nTrainee " + username + " updated: " + trainee + "\n");

        UpdateUserActiveRequest request5 = new UpdateUserActiveRequest();
        request5.setIdentity(traineeProfile.getUsername());
        request5.setPassword(traineeProfile.getPassword());
        request5.setUsername(username);
        request5.setActive(false);
        trainee = traineeService.setActiveStatus(request5);
        System.out.println("\nTrainee " + username + " updated active status: " + trainee + "\n");
        request5.setActive(true);
        trainee = traineeService.setActiveStatus(request5);
        System.out.println("\nTrainee " + username + " updated active status: " + trainee + "\n");
        trainee = traineeService.setActiveStatus(request5);
        System.out.println("\nTrainee " + username + " updated active status: " + trainee + "\n");

        UpdateTraineeTrainersRequest request6 = new UpdateTraineeTrainersRequest();
        request6.setIdentity(traineeProfile.getUsername());
        request6.setPassword(traineeProfile.getPassword());
        request6.setUsername(username);
        request6.setTrainersUsernames(List.of("Jane.Doe", "Jane.Doe.1"));
        trainee = traineeService.updateTrainers(request6);
        System.out.println("\nTrainee " + username + " updated trainers: " + trainee.getTrainers() + "\n");
        request6.setTrainersUsernames(List.of("Jane.Doe"));
        trainee = traineeService.updateTrainers(request6);
        System.out.println("\nTrainee " + username + " updated trainers: " + trainee.getTrainers() + "\n");
    }
}