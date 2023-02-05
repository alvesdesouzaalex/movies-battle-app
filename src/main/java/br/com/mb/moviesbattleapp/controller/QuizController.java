package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizRequest;
import br.com.mb.moviesbattleapp.service.answer.QuizGameAnswerService;
import br.com.mb.moviesbattleapp.service.game.QuizGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {


    private final QuizGameService quizGameService;
    private final QuizGameAnswerService quizGameAnswerService;

    @Autowired
    public QuizController(QuizGameService quizGameService, QuizGameAnswerService quizGameAnswerService) {
        this.quizGameService = quizGameService;
        this.quizGameAnswerService = quizGameAnswerService;
    }

    @GetMapping
    public ResponseEntity<QuizBasic> getCurrentQuiz() {
        var response = this.quizGameService.getCurrentQuiz();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<QuizBasic> answerQuiz(@RequestBody QuizRequest request) {
        var response = this.quizGameAnswerService.answerQuiz(request);
        return ResponseEntity.ok(response);
    }
}
