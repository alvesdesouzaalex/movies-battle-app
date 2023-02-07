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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

    public Quiz findBYid(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(QUIZ_NOT_FOUND));
    }

    public List<Quiz> findAllByPrayer(UserInfo userInfo) {
        return this.repository.findAllByUserInfo(userInfo);
    }

    public Quiz save(Quiz quiz) {
        return this.repository.save(quiz);
    }

    public QuizBasic getCurrentQuiz() {
        this.movieService.loadMovies();

        var userInfo = userService.getLoggedUser();
        Quiz quiz = this.repository.findByUserInfoAndOpenedIsTrue(userInfo);

        if (null == quiz) {

            List<Movie> currentBattle = this.movieService.getABattle();
            quiz = Quiz.builder()
                    .rate(new BigDecimal(BigInteger.ZERO))
                    .score(0)
                    .userInfo(userInfo)
                    .opened(true)
                    .attempts(0)
                    .movies(currentBattle)
                    .build();

            try {
                quiz = this.save(quiz);
            } catch (DataIntegrityViolationException e) {
                LOGGER.error("DUPLICATED QUIZ AND CONTINUE TO ANOTHER GAME");
                getCurrentQuiz();
            }

        }

        return QuizResponse
                .builder() //NOSONAR
                .id(quiz.getId())
                .movies(this.getMoviesToBattle(quiz.getMovies()))
                .build();
    }

    private List<QuizDto> getMoviesToBattle(List<Movie> movies) {
        List<QuizDto> quizDtoList = new ArrayList<>();
        movies.forEach(movie -> {
            quizDtoList.add(QuizDto.of(movie));
            LOGGER.info(movie.toString());
        });
        return quizDtoList;
    }

}
