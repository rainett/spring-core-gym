package com.rainett.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TraineeTest {
    @Test
    @DisplayName("Updates trainers list correctly")
    void testUpdateTrainers() throws NoSuchFieldException, IllegalAccessException {
        Trainee trainee = new Trainee();
        Field field = Trainee.class.getDeclaredField("trainers");
        field.setAccessible(true);
        field.set(trainee, new HashSet<>());

        assertTrue(trainee.getTrainers().isEmpty());
        Trainer t1 = getTrainer();
        Trainer t2 = getTrainer();
        Trainer t3 = getTrainer();

        trainee.updateTrainers(List.of(t1, t2, t3));
        assertEquals(3, trainee.getTrainers().size());

        trainee.updateTrainers(List.of(t1, t3));
        assertEquals(2, trainee.getTrainers().size());

        trainee.updateTrainers(List.of());
        assertTrue(trainee.getTrainers().isEmpty());
    }

    private Trainer getTrainer() throws NoSuchFieldException, IllegalAccessException {
        Trainer trainer = new Trainer();
        trainer.setId(ThreadLocalRandom.current().nextLong());
        Field field = Trainer.class.getDeclaredField("trainees");
        field.setAccessible(true);
        field.set(trainer, new HashSet<>());
        return trainer;
    }
}