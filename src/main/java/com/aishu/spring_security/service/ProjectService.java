package com.aishu.spring_security.service;

import com.aishu.spring_security.Repository.ProjectRepository;
import com.aishu.spring_security.model.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getProjects(
            String sort,
            List<Long> createdByIds,
            int currentUserId) {

        Specification<Project> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (createdByIds != null && !createdByIds.isEmpty()) {

                List<Long> actualIds = new ArrayList<>();

                for (Long id : createdByIds) {
                    if (id != null) {
                        actualIds.add(id);
                    }
                }

                if (!actualIds.isEmpty()) {
                    predicates.add(
                            root.get("createdBy").get("id").in(actualIds));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sorting = getSort(sort);

        return projectRepository
                .findAll(specification, sorting);
    }

    private Sort getSort(String sort) {

        return switch (sort) {

            case "oldest" ->
                Sort.by(
                        Sort.Direction.ASC,
                        "createdBy");

            case "nameAsc" ->
                Sort.by(
                        Sort.Direction.ASC,
                        "name");

            case "nameDesc" ->
                Sort.by(
                        Sort.Direction.DESC,
                        "name");

            default ->
                Sort.by(
                        Sort.Direction.DESC,
                        "createdBy");
        };
    }
}
