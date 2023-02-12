package br.com.mb.moviesbattleapp.service.quiz;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.domain.quiz.QuizResponse;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.QuizRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static br.com.mb.moviesbattleapp.exception.MessageErrors.QUIZ_NOT_FOUND;

@Service
public class QuizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private QuizRepository repository;

    @Autowired
    private UserService userService;

    public Quiz findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(QUIZ_NOT_FOUND));
    }

    public List<Quiz> findAllByPlayer(UserInfo userInfo) {
        return this.repository.findAllByUserInfo(userInfo);
    }

    public Quiz save(Quiz quiz) {
        return this.repository.save(quiz);
    }

    public QuizBasic getCurrentQuiz() {
        this.loadMovies();

        var userInfo = userService.getLoggedUser();
        Quiz quiz = this.repository.findByUserInfoAndOpenedIsTrue(userInfo);

        if (null == quiz) {

            List<Movie> currentBattle = getMovies(userInfo);
            quiz = Quiz.builder()
                    .rate(new BigDecimal(BigInteger.ZERO))
                    .score(0)
                    .userInfo(userInfo)
                    .opened(true)
                    .attempts(0)
                    .movies(currentBattle)
                    .build();

            quiz = this.save(quiz);
        }

        return QuizResponse
                .builder() //NOSONAR
                .id(quiz.getId())
                .movies(this.getMoviesToBattle(quiz.getMovies()))
                .build();
    }

    private List<Movie> getMovies(UserInfo userInfo) {

        List<Movie> currentBattle = this.movieService.getABattle();
        List<Integer> moviesIds = currentBattle.stream().map(Movie::getId).toList();
        List<Quiz> allQuizFromCurrentUser = this.findAllByPlayer(userInfo);
        if (!allQuizFromCurrentUser.isEmpty()) {
            List<Movie> resultList = new ArrayList<>();
            allQuizFromCurrentUser.forEach(quiz1 -> resultList.addAll(quiz1.getMovies()));

            List<Integer> compareTo = resultList.stream().map(Movie::getId).toList();
            if (new HashSet<>(compareTo).containsAll(moviesIds)) {
                getMovies(userInfo);
            }
        }
        return currentBattle;
    }

    private List<QuizDto> getMoviesToBattle(List<Movie> movies) {
        List<QuizDto> quizDtoList = new ArrayList<>();
        movies.forEach(movie -> {
            quizDtoList.add(QuizDto.of(movie));
            LOGGER.info(movie.toString());
        });
        return quizDtoList;
    }

    private void loadMovies() {
        this.movieService.loadMovies(MovieService.movies);
    }

}
