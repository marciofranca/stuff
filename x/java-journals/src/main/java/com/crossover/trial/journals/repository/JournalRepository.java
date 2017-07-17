package com.crossover.trial.journals.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;

@Repository
public interface JournalRepository extends CrudRepository<Journal, Long> {

	Collection<Journal> findByPublisher(Publisher publisher);

	List<Journal> findByCategoryIdIn(Set<Long> ids);

	List<Journal> findByPublishDateBetween(Date start, Date end);

}
