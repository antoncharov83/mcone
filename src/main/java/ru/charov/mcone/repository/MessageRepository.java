package ru.charov.mcone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.charov.mcone.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
