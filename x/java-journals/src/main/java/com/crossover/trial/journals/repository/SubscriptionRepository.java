package com.crossover.trial.journals.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;

/**
 * Repository to handle database manipulation for object {@link Subscription}.
 * 
 * @author marciofranca
 *
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Collection<Subscription> findByCategory(Category category);

	List<Subscription> findByCategoryIdInOrderByUserId(Set<Long> ids);
}
