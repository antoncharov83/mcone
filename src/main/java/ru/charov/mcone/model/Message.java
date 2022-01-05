package ru.charov.mcone.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer session_id;

    private LocalDateTime MC1_timestamp;

    private LocalDateTime MC2_timestamp;

    private LocalDateTime MC3_timestamp;

    private LocalDateTime end_timestamp;
}
