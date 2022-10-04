package org.bk.sample.web;

import org.bk.sample.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @GetMapping("/greetings")
    public ResponseEntity<Message> greetings() {
        return ResponseEntity.ok(new Message("hello"));
    }

    @GetMapping(path = "/greetings", params = "payload")
    public ResponseEntity<Message> greetings(@RequestParam("payload") String payload) {
        return ResponseEntity.ok(new Message(payload));
    }
}
