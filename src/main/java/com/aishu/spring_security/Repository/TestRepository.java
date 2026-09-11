package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface TestRepository extends JpaRepository<TestEntity, Integer> {

}
