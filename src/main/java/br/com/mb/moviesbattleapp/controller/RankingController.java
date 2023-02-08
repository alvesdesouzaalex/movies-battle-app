package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.ranking.RankingResponse;
import br.com.mb.moviesbattleapp.service.ranking.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking endpoint")
@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {


    @Autowired
    private RankingService service;

    @Operation(summary = "Method to find the actual Ranking")
    @GetMapping
    public ResponseEntity<List<RankingResponse>> find() {
        List<RankingResponse> response = this.service.getRanking();
        return ResponseEntity.ok(response);
    }
}
