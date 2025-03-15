package com.example.userauthenticationservice.repos;

import com.example.userauthenticationservice.models.Session;
import com.example.userauthenticationservice.models.Status;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session,Long> {
    Session save(Session session);

    Optional<Session> findByTokenAndUser_IdAndStatus(String token, Long userId, Status status);
}
