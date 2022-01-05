package ru.charov.mcone.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.charov.mcone.model.Message;
import ru.charov.mcone.repository.MessageRepository;
import ru.charov.mcone.websocket.MC1StompSessionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class MC1Service {
    private static final String URL = "ws://mctwo:9090/ws";

    @Setter
    private LocalDateTime startTime;

    @Value("${mcone.process.time}")
    private Long processTime;

    @Setter
    private long count = 0;

    private int sessionId = 0;

    private WebSocketClient client;
    private WebSocketStompClient stompClient;
    private StompSession session;

    private MessageRepository repository;

    private GsonMessageConverter gsonMessageConverter;

    public MC1Service(MessageRepository repository, GsonMessageConverter gsonMessageConverter) {
        this.repository = repository;
        this.gsonMessageConverter = gsonMessageConverter;
        start();
    }

    public void incSession() {
        sessionId++;
    }

    public void start() {
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(gsonMessageConverter);

        StompSessionHandler sessionHandler = new MC1StompSessionHandler();
        try {
            session = stompClient.connect(URL, sessionHandler).get();
        } catch (InterruptedException e) {
            log.error(String.format("Error to start websocket : "), e);
        } catch (ExecutionException e) {
            log.error(String.format("Error to start websocket : "), e);
        }
    }

    public void send() {
        if (Objects.isNull(session) || !Boolean.TRUE.equals(stompClient.isRunning())) {
            start();
        }

        var msg = Message.builder()
                .MC1_timestamp(LocalDateTime.now())
                .session_id(sessionId)
                .build();

        session.send("/mc2/send", msg);

        count += 1;
    }

    public Boolean isCanSend() {
        return Objects.nonNull(startTime) && startTime.plusSeconds(processTime).isAfter(LocalDateTime.now());
    }

    public void save(Message msg) {
        repository.saveAndFlush(msg);
    }

    public void printResult() {
        log.info(String.format("Count generated message : %d, Process time : %d sec", count,
                startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS)));
    }
}
