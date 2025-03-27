package com.rainett.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainee_id", nullable = false,
            foreignKey = @ForeignKey(name = "training_trainee_fk"))
    private Trainee trainee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false,
            foreignKey = @ForeignKey(name = "training_trainer_fk"))
    private Trainer trainer;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "training_type_id",
            foreignKey = @ForeignKey(name = "training_training_type_fk"))
    private TrainingType trainingType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long duration;
}
