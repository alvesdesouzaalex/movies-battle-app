package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;

import java.math.BigDecimal;
import java.util.List;

public class QuizFixture {

    public static Quiz getQuiz(Integer id, List<Movie> movies, Integer attempts, boolean opened, BigDecimal rate, Integer score) {
        return Quiz.builder()
                .id(id)
                .attempts(attempts)
                .movies(movies)
                .rate(rate)
                .opened(opened)
                .score(score)
                .build();
    }
}
