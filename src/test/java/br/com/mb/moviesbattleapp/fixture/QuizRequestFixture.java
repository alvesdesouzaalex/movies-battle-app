package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.domain.quiz.QuizRequest;

import java.math.BigDecimal;
import java.util.List;

public class QuizRequestFixture {


    public static QuizBasic quizRequest() {
        return QuizRequest.builder()
                .id(1)
                .movies(List.of(quizDto("tt13766270", 8.2), quizDto("tt13766272", 7.7)))
                .build();
    }

    private static QuizDto quizDto(String imdbID, double rate) {
        return QuizDto.builder()
                .imdbID(imdbID)
                .rate(BigDecimal.valueOf(rate))
                .build();
    }
}
