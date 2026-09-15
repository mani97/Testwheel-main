package com.aishu.spring_security.controller.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.aishu.spring_security.Dto.APITestingDto;
import com.aishu.spring_security.Dto.MobileTestingDto;
import com.aishu.spring_security.Dto.ProjectCardDTO;
import com.aishu.spring_security.Dto.Step2formDto;
import com.aishu.spring_security.Dto.TestEntityDto;
import com.aishu.spring_security.Dto.UserDto;
import com.aishu.spring_security.Dto.WebTestingDto;
import com.aishu.spring_security.model.TestEntity;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.Project;

public class Mapper {

    // Convert TestEntityDto → WebTestingDto (only selected fields)
    public static WebTestingDto toWebTestingDto(TestEntityDto dto) {
        WebTestingDto webDto = new WebTestingDto();
        webDto.setTestName(dto.getTestName());
        webDto.setModules(dto.getModules());
        webDto.setTag(dto.getTag());
        webDto.setDescription(dto.getDescription());
        webDto.setWebsiteUrl(dto.getWebsiteUrl());
        webDto.setBackendApiTest(dto.isBackendApiTest());
        webDto.setProjectId(dto.getProjectId());
        return webDto;
    }

    // Convert WebTestingDto → TestEntity (for persistence)
    public static TestEntity toEntity(WebTestingDto webDto) {
        TestEntity entity = new TestEntity();
        entity.setTestName(webDto.getTestName());
        entity.setModules(webDto.getModules());
        entity.setTag(webDto.getTag());
        entity.setDescription(webDto.getDescription());
        entity.setWebsiteUrl(webDto.getWebsiteUrl());
        entity.setBackendApiTest(webDto.isBackendApiTest());
        entity.setProject(webDto.getProject());
        return entity;
    }

    public static MobileTestingDto toMobileTestingDto(TestEntityDto dto) {
        MobileTestingDto mobileDto = new MobileTestingDto();
        mobileDto.setTestName(dto.getTestName());
        mobileDto.setModules(dto.getModules());
        mobileDto.setDescription(dto.getDescription());
        mobileDto.setApiProtocol(dto.getApiProtocol());
        mobileDto.setTag(dto.getTag());
        mobileDto.setProjectId(dto.getProjectId());
        return mobileDto;
    }

    public static APITestingDto toAPITestingDto(TestEntityDto dto) {
        APITestingDto apiDto = new APITestingDto();
        apiDto.setTestName(dto.getTestName());
        apiDto.setModules(dto.getModules());
        apiDto.setDescription(dto.getDescription());
        apiDto.setApiProtocol("HTTP/REST");
        apiDto.setTag(dto.getTag());
        apiDto.setProjectId(dto.getProjectId());
        return apiDto;
    }

    public static TestEntity toEntity(APITestingDto apiDto) {
        TestEntity entity = new TestEntity();
        entity.setTestName(apiDto.getTestName());
        entity.setModules(apiDto.getModules());
        entity.setDescription(apiDto.getDescription());
        entity.setTag(apiDto.getTag());
        entity.setApiProtocol("HTTP/REST");
        entity.setProject(apiDto.getProject());
        return entity;
    }

    public static Step2formDto toStep2formDto(TestEntityDto dto) {
        Step2formDto step2formDto = new Step2formDto();
        step2formDto.setTestName(dto.getTestName());
        step2formDto.setModules(dto.getModules());
        step2formDto.setTag(dto.getTag());
        step2formDto.setDescription(dto.getDescription());
        return step2formDto;
    }

    public static ProjectCardDTO toProjectCardDto(Project projects) {
        ProjectCardDTO projectCardDto = new ProjectCardDTO();
        projectCardDto.setProjectId(projects.getProjectId());
        projectCardDto.setProjectName(projects.getProjectName());
        projectCardDto.setProjectUrl(projects.getProjectUrl());
        projectCardDto.setTestCases(projects.getTests() != null ? projects.getTests().size() : 0);
        projectCardDto.setUsers(projects.getUsername() != null ? 1 : 0);
        projectCardDto.setCreatedBy(projects.getCreatedBy() != null ? projects.getCreatedBy().getFirstName() : "");
        projectCardDto.setCreatedAt(projects.getCreatedAt());
        return projectCardDto;
    }

    // public static Project toProject(ProjectCardDTO projectCardDto) {
    // Project project = new Project();
    // project.setProjectId(projectCardDto.getProjectId());
    // project.setProjectName(projectCardDto.getProjectName());
    // project.setProjectUrl(projectCardDto.getProjectUrl());
    // project.setCreatedAt(projectCardDto.getCreatedAt());

    // project.setCreatedBy(User user);
    // return project;
    // }

    public static User userDtoToEntity(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setConfirmPassword(userDto.getConfirmPassword());

        user.setEnabled(userDto.isEnabled());

        return user;
    }

    public static List<ProjectCardDTO> toProjectCardDto(List<Project> projects) {
        return projects.stream()
                .map(Mapper::toProjectCardDto) // reuse single mapper
                .collect(Collectors.toList());
    }

}
