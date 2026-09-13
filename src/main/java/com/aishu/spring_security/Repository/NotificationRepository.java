package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.Notification;
import com.aishu.spring_security.model.User;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<Notification, Date> {

    List<Notification> findTop5ByOrderByTimeAgoDesc();

    List<Notification> findByUseridOrderByTimeAgoDesc(User currentUser);

    @Query("SELECT n FROM Notification n WHERE n.userid = :user ORDER BY n.timeAgo DESC")
    List<Notification> findAllByUseridOrderByTimeAgoDesc(@Param("user") User currentUser);

    List<Notification> findByUserid(User user);

    @Query("SELECT n FROM Notification n WHERE n.userid = :user ORDER BY n.timeAgo DESC")
    List<Notification> findRecentByUserid(@Param("user") User currentUser, Pageable pageable);

}
