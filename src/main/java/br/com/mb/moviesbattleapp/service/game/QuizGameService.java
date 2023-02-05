package br.com.mb.moviesbattleapp.service.game;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.domain.quiz.QuizResponse;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizGameService {

    private final MovieService movieService;

    @Autowired
    public QuizGameService(MovieService movieService) {
        this.movieService = movieService;
    }

    public QuizBasic getCurrentQuiz() {
        //TODO refatorar para fazer um strream na lista e retornar
        List<QuizDto> quizDtoList = new ArrayList<>();
        this.movieService.getCurrentBattle()
                .forEach(movie ->
                        quizDtoList.add(QuizDto.of(movie))
                );
        return QuizResponse.builder().movies(quizDtoList).build();
    }


}
