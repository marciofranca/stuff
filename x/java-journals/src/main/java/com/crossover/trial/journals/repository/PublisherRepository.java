package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Optional<Publisher> findByUser(User user);

}
