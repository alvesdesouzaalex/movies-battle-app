package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.fixture.MoviesFixture;
import br.com.mb.moviesbattleapp.fixture.QuizFixture;
import br.com.mb.moviesbattleapp.fixture.QuizRequestFixture;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.service.answer.QuizGameAnswerService;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.ranking.RankingService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuizGameAnswerServiceTest {

    @TestConfiguration
    static class QuizGameAnswerServiceTestConfiguration {
        @Bean
        public QuizGameAnswerService quizGameAnswerService() {
            return new QuizGameAnswerService();
        }
    }

    @Autowired
    QuizGameAnswerService service;

    @MockBean
    QuizService quizService;
    @MockBean
    MovieService movieService;
    @MockBean
    UserService userService;
    @MockBean
    RankingService rankingService;


    @Test
    public void answerQuizRight_Success() {

        QuizBasic quizBasic = QuizRequestFixture.quizRequest();

        QuizDto quizDtoWinner = quizBasic.getMovies()
                .stream().max(Comparator.comparing(QuizDto::getRate))
                .orElseThrow(RuntimeException::new);

        Movie movieWinner = MoviesFixture.movie("title1", quizDtoWinner.getImdbID(), "poster1", 8.2, 1);

        when(movieService.findByImdbID(quizDtoWinner.getImdbID())).thenReturn(movieWinner);


        Quiz saved = QuizFixture.getQuiz(1, List.of(movieWinner), 0, true, BigDecimal.valueOf(8), 7);
        when(quizService.findById(quizBasic.getId())).thenReturn(saved);

        QuizAttemptsResponse response = service.answerQuiz(quizBasic);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("status", equalTo("You win. Try a new Quiz.")));
        assertThat(response, hasProperty("failedAttempts", equalTo(saved.getAttempts())));
        assertThat(response, hasProperty("maxAttempts", equalTo(3)));
    }


    @Test
    public void answerQuizWrong_Success() {

        QuizBasic quizBasic = QuizRequestFixture.quizRequest();

        QuizDto quizDtoWinner = quizBasic.getMovies()
                .stream().max(Comparator.comparing(QuizDto::getRate))
                .orElseThrow(RuntimeException::new);

        Movie movieWinner = MoviesFixture.movie("title1", quizDtoWinner.getImdbID(), "poster1", 8, 1);

        when(movieService.findByImdbID(quizDtoWinner.getImdbID())).thenReturn(movieWinner);


        Quiz saved = QuizFixture.getQuiz(1, List.of(movieWinner), 0, true, BigDecimal.valueOf(8), 7);
        when(quizService.findById(quizBasic.getId())).thenReturn(saved);

        QuizAttemptsResponse response = service.answerQuiz(quizBasic);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("status", equalTo("You lose, try again.")));
        assertThat(response, hasProperty("failedAttempts", equalTo(1)));
        assertThat(response, hasProperty("maxAttempts", equalTo(3)));
    }


    @Test
    public void answerQuizWrongAndQuizIsClosed_Success() {

        QuizBasic quizBasic = QuizRequestFixture.quizRequest();

        QuizDto quizDtoWinner = quizBasic.getMovies()
                .stream().max(Comparator.comparing(QuizDto::getRate))
                .orElseThrow(RuntimeException::new);

        Movie movieWinner = MoviesFixture.movie("title1", quizDtoWinner.getImdbID(), "poster1", 8, 1);

        when(movieService.findByImdbID(quizDtoWinner.getImdbID())).thenReturn(movieWinner);


        Quiz saved = QuizFixture.getQuiz(1, List.of(movieWinner), 0, false, BigDecimal.valueOf(8), 7);
        when(quizService.findById(quizBasic.getId())).thenReturn(saved);

        QuizAttemptsResponse response = service.answerQuiz(quizBasic);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("status", equalTo("Game over, ask for a new game")));
        assertThat(response, hasProperty("failedAttempts", equalTo(1)));
        assertThat(response, hasProperty("maxAttempts", equalTo(3)));
    }


    @Test
    public void answerQuizFinishedGame_Success() {

        QuizBasic quizBasic = QuizRequestFixture.quizRequest();

        QuizDto quizDtoWinner = quizBasic.getMovies()
                .stream().max(Comparator.comparing(QuizDto::getRate))
                .orElseThrow(RuntimeException::new);

        Movie movieWinner = MoviesFixture.movie("title1", quizDtoWinner.getImdbID(), "poster1", 8, 1);

        when(movieService.findByImdbID(quizDtoWinner.getImdbID())).thenReturn(movieWinner);


        Quiz saved = QuizFixture.getQuiz(1, List.of(movieWinner), 2, true, BigDecimal.valueOf(8), 7);
        when(quizService.findById(quizBasic.getId())).thenReturn(saved);

        QuizAttemptsResponse response = service.answerQuiz(quizBasic);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("status", equalTo("Game over, ask for a new game")));
        assertThat(response, hasProperty("failedAttempts", equalTo(3)));
        assertThat(response, hasProperty("maxAttempts", equalTo(3)));
    }

    @Test
    public void answerQuizFinishedGameAndClosed_Success() {

        QuizBasic quizBasic = QuizRequestFixture.quizRequest();

        QuizDto quizDtoWinner = quizBasic.getMovies()
                .stream().max(Comparator.comparing(QuizDto::getRate))
                .orElseThrow(RuntimeException::new);

        Movie movieWinner = MoviesFixture.movie("title1", quizDtoWinner.getImdbID(), "poster1", 8, 1);

        when(movieService.findByImdbID(quizDtoWinner.getImdbID())).thenReturn(movieWinner);


        Quiz saved = QuizFixture.getQuiz(1, List.of(movieWinner), 0, false, BigDecimal.valueOf(8), 7);
        when(quizService.findById(quizBasic.getId())).thenReturn(saved);

        QuizAttemptsResponse response = service.answerQuiz(quizBasic);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("status", equalTo("Game over, ask for a new game")));
        assertThat(response, hasProperty("failedAttempts", equalTo(1)));
        assertThat(response, hasProperty("maxAttempts", equalTo(3)));
    }


}
