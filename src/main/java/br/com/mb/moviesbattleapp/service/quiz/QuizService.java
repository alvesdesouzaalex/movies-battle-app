package br.com.mb.moviesbattleapp.service.quiz;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.domain.quiz.QuizResponse;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.repository.QuizRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class QuizService {

    @Autowired
    private MovieService movieService;

    @Autowired
    private QuizRepository repository;

    @Autowired
    private UserService userService;

    public Quiz findBYid(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public Quiz save(Quiz quiz) {
        return this.repository.save(quiz);
    }

    public QuizBasic getCurrentQuiz() {
        List<Movie> moviesBattleList;
        List<QuizDto> moviesBattleListDto;


        CompletableFuture<List<Movie>> currentBattle = this.movieService.getCurrentBattle();
        var userInfo = userService.getLoggedUser();
        Quiz quiz = this.repository.findByUserInfoAndOpenedIsTrue(userInfo);

        if (null == quiz) {

            try {
                moviesBattleList =  currentBattle.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            moviesBattleListDto = this.getMoviesToBattle(moviesBattleList);

            quiz = Quiz.builder()
                    .rate(new BigDecimal(BigInteger.ZERO))
                    .score(0)
                    .userInfo(userInfo)
                    .opened(true)
                    .attemptsFailedMap("")
                    .attempts(0)
                    .movies(moviesBattleList)
                    .build();

            quiz = this.save(quiz);

        } else {
            //TODO verificar pois nao eta trazando os filmes correntes
            moviesBattleListDto = getMoviesToBattle(quiz.getMovies());
        }

        return QuizResponse
                .builder() //NOSONAR
                .id(quiz.getId())
                .movies(moviesBattleListDto).build();
    }

    private List<QuizDto> getMoviesToBattle(List<Movie> movies) {
        List<QuizDto> quizDtoList = new ArrayList<>();
        movies.forEach(movie ->  { quizDtoList.add(QuizDto.of(movie)); System.out.println(movie); });
        return quizDtoList;
    }

}
