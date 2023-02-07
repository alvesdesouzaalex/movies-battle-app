package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizRequest;
import br.com.mb.moviesbattleapp.service.answer.QuizGameAnswerService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quiz endpoint")
@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizGameAnswerService quizGameAnswerService;


    @Operation(summary = "Endpoint to find Current Quiz by logged User")
    @GetMapping
    public ResponseEntity<QuizBasic> getCurrentQuiz() {
        var response = this.quizService.getCurrentQuiz();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Endpoint to answer Current Quiz by logged User")
    @PostMapping
    public ResponseEntity<QuizAttemptsResponse> answerQuiz(@RequestBody QuizRequest request) {
        var response = this.quizGameAnswerService.answerQuiz(request);
        return ResponseEntity.ok(response);
    }
}
