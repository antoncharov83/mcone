package ru.charov.mcone.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.charov.mcone.model.Message;
import ru.charov.mcone.service.MC1Service;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mc1")
@Slf4j
public class MC1Controller {

    private final MC1Service mc1Service;

    private final Gson mapper;

    @GetMapping("/start")
    public ResponseEntity start() {
        if (!mc1Service.isCanSend()) {
            mc1Service.setStartTime(LocalDateTime.now());
            mc1Service.setCount(0);
            mc1Service.incSession();
            mc1Service.send();

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.IM_USED).build();
    }

    @GetMapping("/stop")
    public ResponseEntity stop() {
        mc1Service.setStartTime(LocalDateTime.MIN);
        mc1Service.printResult();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity send(@RequestBody String msgTxt) {
        var msg = mapper.fromJson(msgTxt, Message.class);
        msg.setEnd_timestamp(LocalDateTime.now());
        mc1Service.save(msg);
        if (mc1Service.isCanSend()) {
            mc1Service.send();
        } else {
            mc1Service.printResult();
        }

        return ResponseEntity.ok().build();
    }
}
