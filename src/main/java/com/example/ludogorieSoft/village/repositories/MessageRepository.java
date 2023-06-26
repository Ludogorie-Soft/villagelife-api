package com.example.ludogoriesoft.village.repositories;

import com.example.ludogoriesoft.village.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
