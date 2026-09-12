package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.Notification;
import com.aishu.spring_security.model.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;

@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<Notification, Date> {

    List<Notification> findTop5ByOrderByTimeAgoDesc();

    List<Notification> findByUseridOrderByTimeAgoDesc(User currentUser);

    List<Notification> findAllByUseridOrderByTimeAgoDesc(User currentUser);

    List<Notification> findByUserid(User user);

    List<Notification> findRecentByUserid(User currentUser, PageRequest of);
}
