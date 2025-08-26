package dev.francode.ordersystem.repository;

import dev.francode.ordersystem.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Long> {
    boolean existsByEmail(String email);
    Optional<UserApp> findByEmail(String email);
}
