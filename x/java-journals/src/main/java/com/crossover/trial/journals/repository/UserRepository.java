package com.crossover.trial.journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.trial.journals.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByLoginName(String loginName);

}
