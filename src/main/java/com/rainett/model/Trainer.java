package com.rainett.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
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
@Table(name = "trainers")
public class Trainer extends User {
    @ManyToOne(optional = false)
    @JoinColumn(name = "training_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "trainer_training_type_fk"))
    private TrainingType specialization;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "trainers_trainees",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"),
            foreignKey = @ForeignKey(name = "trainer_trainee_fk"),
            inverseForeignKey = @ForeignKey(name = "trainee_trainer_fk")
    )
    @Setter(AccessLevel.PRIVATE)
    private Set<Trainee> trainees = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "trainer", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter(AccessLevel.PRIVATE)
    private Set<Training> trainings = new HashSet<>();
}
