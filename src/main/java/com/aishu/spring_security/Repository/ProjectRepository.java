package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.Project;
import com.aishu.spring_security.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

    List<Project> findAllByOrderByCreatedAtDesc();

    List<Project> findByCreatedByIdInOrderByCreatedAtDesc(
            List<Long> userIds);

    List<Project> findByCreatedBy(User currentUser);

    List<Project> findByUsername(User currentUser);

}
