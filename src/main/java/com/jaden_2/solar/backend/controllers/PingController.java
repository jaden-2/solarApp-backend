package com.jaden_2.solar.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    @GetMapping
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("pong");
    }
}
