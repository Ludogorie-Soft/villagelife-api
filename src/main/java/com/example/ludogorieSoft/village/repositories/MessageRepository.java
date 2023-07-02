package com.example.ludogorieSoft.village.repositories;

import com.example.ludogorieSoft.village.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
