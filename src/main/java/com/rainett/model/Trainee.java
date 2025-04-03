package com.rainett.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "trainees")
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    @ToString.Exclude
    @ManyToMany(mappedBy = "trainees", cascade = CascadeType.MERGE)
    @Setter(AccessLevel.PRIVATE)
    private Set<Trainer> trainers;

    @ToString.Exclude
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE)
    private Set<Training> trainings;

    public void updateTrainers(List<Trainer> trainers) {
        Set<Trainer> newTrainersSet = new HashSet<>(trainers);
        Set<Trainer> currentTrainers = new HashSet<>(this.trainers);

        for (Trainer trainer : currentTrainers) {
            if (!newTrainersSet.contains(trainer)) {
                trainer.getTrainees().remove(this);
                this.trainers.remove(trainer);
            }
        }

        for (Trainer trainer : newTrainersSet) {
            if (!currentTrainers.contains(trainer)) {
                trainer.getTrainees().add(this);
                this.trainers.add(trainer);
            }
        }
    }
}
