package br.com.mb.moviesbattleapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {


    @GetMapping
    public ResponseEntity<Object> getTest() {
        return ResponseEntity.ok("The winner is Alex Alves de Souza");
    }
}
