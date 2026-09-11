package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.ApkUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ApkRepo extends JpaRepository<ApkUpload,Long> {


}
