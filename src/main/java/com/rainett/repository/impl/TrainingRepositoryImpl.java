package com.rainett.repository.impl;

import com.rainett.dto.training.FindTraineeTrainingsRequest;
import com.rainett.dto.training.FindTrainerTrainingsRequest;
import com.rainett.model.Training;
import com.rainett.repository.TrainingRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TrainingRepositoryImpl extends AbstractHibernateRepository<Training>
        implements TrainingRepository {
    private static final String USERNAME_FIELD = "username";
    private static final String DATE_FIELD = "date";

    public TrainingRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Training> findTraineeTrainings(FindTraineeTrainingsRequest request) {
        log.debug("Finding trainee trainings for request {}", request);
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);

        Predicate predicate = initPredicate(request.getUsername(), "trainee", cb, root);
        predicate = addConditionIfNotNull(cb, predicate, root, request.getFrom(),
                (r, value) -> cb.greaterThanOrEqualTo(r.get(DATE_FIELD), value));
        predicate = addConditionIfNotNull(cb, predicate, root, request.getTo(),
                (r, value) -> cb.lessThanOrEqualTo(r.get(DATE_FIELD), value));
        predicate = addConditionIfNotNull(cb, predicate, root, request.getTrainerUsername(),
                (r, value) -> cb.equal(r.get("trainer").get(USERNAME_FIELD), value));
        predicate = addConditionIfNotNull(cb, predicate, root, request.getTrainingType(),
                (r, value) -> cb.equal(r.get("trainingType"), value));
        cq.where(predicate);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainings(FindTrainerTrainingsRequest request) {
        log.debug("Finding trainer trainings for request {}", request);
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);

        Predicate predicate = initPredicate(request.getUsername(), "trainer", cb, root);
        predicate = addConditionIfNotNull(cb, predicate, root, request.getFrom(),
                (r, value) -> cb.greaterThanOrEqualTo(r.get(DATE_FIELD), value));
        predicate = addConditionIfNotNull(cb, predicate, root, request.getTo(),
                (r, value) -> cb.lessThanOrEqualTo(r.get(DATE_FIELD), value));
        predicate = addConditionIfNotNull(cb, predicate, root, request.getTraineeUsername(),
                (r, value) -> cb.equal(r.get("trainee").get(USERNAME_FIELD), value));

        cq.where(predicate);
        return session.createQuery(cq).getResultList();
    }

    private static Predicate initPredicate(String requestUsername,
                                           String fieldName,
                                           CriteriaBuilder cb,
                                           Root<Training> root) {
        return cb.equal(
                root.get(fieldName).get(USERNAME_FIELD),
                requestUsername
        );
    }

    private <T> Predicate addConditionIfNotNull(
            CriteriaBuilder cb,
            Predicate existingPredicate,
            Root<Training> root,
            T value,
            BiFunction<Root<Training>, T, Predicate> conditionBuilder
    ) {
        if (value == null) {
            return existingPredicate;
        }
        Predicate newCondition = conditionBuilder.apply(root, value);
        return cb.and(existingPredicate, newCondition);
    }
}
