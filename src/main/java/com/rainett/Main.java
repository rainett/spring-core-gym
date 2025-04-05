package com.rainett;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.facade.TrainingFacade;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.service.TraineeService;
import com.rainett.service.TrainerService;
import com.rainett.service.TrainingService;
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
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAddress("123 Main St");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Trainee traineeProfile = traineeService.createProfile(request);
        traineeService.createProfile(request);
        traineeService.createProfile(request);

        TrainerService trainerService = trainingFacade.getTrainerService();
        CreateTrainerRequest request1 = new CreateTrainerRequest();
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
        trainee = traineeService.updateTrainers(username, request6);
        System.out.println("\nTrainee " + username + " updated trainers: " + trainee.getTrainers() + "\n");
        request6.setTrainersUsernames(List.of("Jane.Doe"));
        trainee = traineeService.updateTrainers(username, request6);
        System.out.println("\nTrainee " + username + " updated trainers: " + trainee.getTrainers() + "\n");

        UsernameRequest request7 = new UsernameRequest();
        request7.setIdentity(traineeProfile.getUsername());
        request7.setPassword(traineeProfile.getPassword());
        String trainerUsername = "Jane.Doe";
        request7.setUsername(trainerUsername);
        Trainer trainer = trainerService.findByUsername(request7);
        System.out.println("\nTrainer " + trainerUsername + " found: " + trainer + "\n");

        UpdatePasswordRequest request8 = new UpdatePasswordRequest();
        request8.setIdentity(traineeProfile.getUsername());
        request8.setPassword(traineeProfile.getPassword());
        request8.setUsername(trainerUsername);
        request8.setNewPassword("newPassword");
        trainer = trainerService.updatePassword(request8);
        System.out.println("\nTrainer " + trainerUsername + " updated password: " + trainer + "\n");

        UpdateTrainerRequest request9 = new UpdateTrainerRequest();
        request9.setIdentity(traineeProfile.getUsername());
        request9.setPassword(traineeProfile.getPassword());
        request9.setUsername(trainerUsername);
        request9.setFirstName("Sarah");
        request9.setLastName("Doe");
        request9.setSpecialization("Boxing");
        trainer = trainerService.updateTrainer(request9);
        trainerUsername = trainer.getUsername();
        System.out.println("\nTrainer " + trainerUsername + " updated: " + trainer + "\n");

        UpdateUserActiveRequest request10 = new UpdateUserActiveRequest();
        request10.setIdentity(traineeProfile.getUsername());
        request10.setPassword(traineeProfile.getPassword());
        request10.setUsername(trainerUsername);
        request10.setActive(false);
        trainer = trainerService.setActiveStatus(request10);
        System.out.println("\nTrainer " + trainerUsername + " updated active status: " + trainer + "\n");
        request10.setActive(true);
        trainer = trainerService.setActiveStatus(request10);
        System.out.println("\nTrainer " + trainerUsername + " updated active status: " + trainer + "\n");
        trainer = trainerService.setActiveStatus(request10);
        System.out.println("\nTrainer " + trainerUsername + " updated active status: " + trainer + "\n");

        UsernameRequest request11 = new UsernameRequest();
        request11.setIdentity(traineeProfile.getUsername());
        request11.setPassword(traineeProfile.getPassword());
        request11.setUsername(username);
        List<Trainer> trainers = trainerService.getTrainersWithoutTraineeByUsername(request11);
        System.out.println("\nTrainers without " + username + " trainee: " + trainers + "\n");

        TrainingService trainingService = trainingFacade.getTrainingService();
        CreateTrainingRequest request12 = new CreateTrainingRequest();
        request12.setIdentity(traineeProfile.getUsername());
        request12.setPassword(traineeProfile.getPassword());
        request12.setTraineeUsername(username);
        request12.setTrainerUsername(trainerUsername);
        request12.setName("Training 1");
        request12.setTrainingType("Boxing");
        request12.setDate(LocalDate.now());
        request12.setDuration(60L);
        Training training = trainingService.createTraining(request12);
        System.out.println("\nTraining created: " + training + "\n");
        request12.setName("Training 2");
        training = trainingService.createTraining(request12);
        System.out.println("\nTraining created: " + training + "\n");
        request12.setName("Training 3");
        training = trainingService.createTraining(request12);
        System.out.println("\nTraining created: " + training + "\n");

        FindTraineeTrainingsRequest request13 = new FindTraineeTrainingsRequest();
        request13.setIdentity(traineeProfile.getUsername());
        request13.setPassword(traineeProfile.getPassword());
        request13.setUsername(username);
        List<Training> trainings = trainingService.findTrainingsForTrainee(request13);
        System.out.println("\nTrainings for " + username + ": " + trainings + "\n");
        request13.setTrainerUsername("non-existing");
        trainings = trainingService.findTrainingsForTrainee(request13);
        System.out.println("\nTrainings for non-existing trainer: " + trainings + "\n");
        request13.setTrainerUsername(trainerUsername);
        trainings = trainingService.findTrainingsForTrainee(request13);
        System.out.println("\nTrainings for " + username + ": " + trainings + "\n");

        FindTrainerTrainingsRequest request14 = new FindTrainerTrainingsRequest();
        request14.setIdentity(traineeProfile.getUsername());
        request14.setPassword(traineeProfile.getPassword());
        request14.setUsername(trainerUsername);
        trainings = trainingService.findTrainingsForTrainer(request14);
        System.out.println("\nTrainings for " + trainerUsername + ": " + trainings + "\n");
        request14.setTraineeUsername("non-existing");
        trainings = trainingService.findTrainingsForTrainer(request14);
        System.out.println("\nTrainings for non-existing trainee: " + trainings + "\n");
        request14.setTraineeUsername(username);
        trainings = trainingService.findTrainingsForTrainer(request14);
        System.out.println("\nTrainings for " + trainerUsername + ": " + trainings + "\n");

        UsernameRequest request15 = new UsernameRequest();
        request15.setIdentity(traineeProfile.getUsername());
        request15.setPassword(traineeProfile.getPassword());
        request15.setUsername(username);
        traineeService.deleteProfile(request15);
        trainings = trainingService.findTrainingsForTrainee(request13);
        System.out.println("\nTrainings for " + username + " deleted as well: " + trainings + "\n");
        trainings = trainingService.findTrainingsForTrainer(request14);
        System.out.println("\nTrainings for " + trainerUsername + " deleted as well: " + trainings + "\n");
        List<Trainee> trainees = trainerService.getTraineesByTrainerUsername(trainerUsername);
        System.out.println("\nTrainees for " + trainerUsername + ": " + trainees + "\n");
    }
}