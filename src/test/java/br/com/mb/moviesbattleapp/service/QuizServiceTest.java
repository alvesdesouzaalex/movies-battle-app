package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.fixture.QuizFixture;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.QuizRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.mb.moviesbattleapp.fixture.MoviesFixture.movie;
import static br.com.mb.moviesbattleapp.fixture.QuizFixture.getQuiz;
import static br.com.mb.moviesbattleapp.fixture.UserInfoFixture.userInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuizServiceTest {


    @TestConfiguration
    static class QuizServiceTestConfiguration {
        @Bean
        public QuizService quizService() {
            return new QuizService();
        }
    }

    @Autowired
    QuizService service;

    @MockBean
    QuizRepository repository;
    @MockBean
    MovieService movieService;
    @MockBean
    UserService userService;


    @Test
    public void findQuizById_Success() {
        Integer id = 1;
        Optional<Quiz> quiz = Optional.of(getQuiz(id, new ArrayList<>(), 0, true, BigDecimal.valueOf(0), 0));
        when(repository.findById(id)).thenReturn(quiz);
        Quiz response = service.findById(id);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("score", equalTo(quiz.get().getScore())));
    }


    @Test(expected = BusinessException.class)
    public void findQuizById_Error_WhenQuizNotFound() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        service.findById(id);
    }

    @Test
    public void findAllByPlayer_Success() {
        UserInfo userInfo = UserInfo.builder().name("user1").build();
        Optional<Quiz> quiz = Optional.of(getQuiz(1, new ArrayList<>(), 0, true, BigDecimal.valueOf(0), 0));
        List<Quiz> quizList = List.of(quiz.get());
        when(repository.findAllByUserInfo(userInfo)).thenReturn(quizList);
        List<Quiz> response = service.findAllByPlayer(userInfo);

        assertThat(response, notNullValue());
        assertThat(response.size(), equalTo(1));
    }


    @Test
    public void getCurrentNewQuiz_Success() {

        UserInfo userInfo = userInfo("user1");
        when(userService.getLoggedUser()).thenReturn(userInfo);
        when(repository.findByUserInfoAndOpenedIsTrue(userInfo)).thenReturn(null);

        List<Movie> currentBattle = List.of(
                movie("title1", "imdb1", "poster1", 2.2, 1),
                movie("title2", "imdb2", "poster2", 5.89, 2),
                movie("title3", "imdb3", "poster3", 7.7, 3)
        );

        when(movieService.getABattle()).thenReturn(currentBattle);

        List<Movie> oldsBattle = List.of(
                movie("title4", "imdb4", "poster4", 2.25, 4),
                movie("title5", "imdb5", "poster5", 6.89, 5),
                movie("title6", "imdb6", "poster6", 8.7, 6)
        );


        when(repository.findAllByUserInfo(userInfo)).thenReturn(List.of(getQuiz(2, oldsBattle, 2, false, BigDecimal.valueOf(100), 5)));

        Quiz quiz = getQuiz(1, currentBattle, 0, true, BigDecimal.valueOf(0), 0);

        when(repository.save(Mockito.any(Quiz.class))).thenReturn(quiz);

        QuizBasic response = service.getCurrentQuiz();

        assertThat(response, notNullValue());
        assertThat(response.getMovies().size(), equalTo(3));
    }


    @Test
    public void saveQuiz_Success() {
        Quiz quiz = getQuiz(1, new ArrayList<>(), 0, true, BigDecimal.valueOf(0), 0);

        when(repository.save(quiz)).thenReturn(quiz);
        Quiz response = service.save(quiz);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("id", equalTo(quiz.getId())));
    }


}
