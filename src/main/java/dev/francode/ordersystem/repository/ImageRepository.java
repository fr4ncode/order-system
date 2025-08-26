package dev.francode.ordersystem.repository;

import dev.francode.ordersystem.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
