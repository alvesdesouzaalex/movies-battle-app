package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private OmdbClient client;

    @GetMapping
    public Object getCurrentQuiz() {
        var result = this.client.getMovies();
        System.out.println(result);
        return result;
    }


    public Object respondQuiz() {
        return null;
    }
}
