package br.com.mb.moviesbattleapp.service.answer;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.quiz.AttemptsConstants;
import br.com.mb.moviesbattleapp.model.quiz.QuizAttempts;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizGameAnswerService {

    @Autowired
    private QuizService quizService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Transactional
    public QuizAttemptsResponse answerQuiz(QuizBasic request) {

        QuizDto quizDtoWinner = request.getMovies().stream().max(Comparator.comparing(QuizDto::getRate)).orElseThrow(RuntimeException::new);
        QuizDto quizDtoLoser = request.getMovies().stream().min(Comparator.comparing(QuizDto::getRate)).orElseThrow(RuntimeException::new);
        Movie movieWinner = this.movieService.findByImdbID(quizDtoWinner.getImdbID());
        Movie movieLoser = this.movieService.findByImdbID(quizDtoLoser.getImdbID());

        Map<String, Object> map = new HashMap<>();
        String status = "you win! game finished.";
        String attemptsFailed = null;
        int score = 0;
        //TODO separar em metodo
        if (!Objects.equals(movieWinner.getRate(), quizDtoWinner.getRate())) {
            map.put(movieWinner.getTitle(), quizDtoWinner.getRate());
            map.put(movieLoser.getTitle(), quizDtoLoser.getRate());
            QuizAttempts.attemptsFailed.put(AttemptsConstants.FIRST, map);
            status = "you lose";

            ObjectMapper mapper = new ObjectMapper();
            try {
                attemptsFailed = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(QuizAttempts.attemptsFailed);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        } else {
            score = 1;
        }


        Quiz saved = this.quizService.findBYid(request.getId());
        int attempts = saved.getAttempts();

        if (attempts >= 3 || Boolean.FALSE.equals(saved.getOpened())) {
            this.inactivateCurrentQuiz(saved);
            return QuizAttemptsResponse.builder()
                    .failedAttempts(attempts)
                    .status("Game over, ask for a new game")
                    .maxAttempts(3)
                    .build();
        }

        attempts++;
        Quiz quiz = Quiz.builder()
                .id(saved.getId())
                .attempts(attempts)
                .attemptsFailedMap(attemptsFailed)
                .rate(quizDtoWinner.getRate())
                .movies(List.of(movieWinner, movieLoser))
                .opened(score == 0)
                .userInfo(this.userService.getLoggedUser())
                .score(score)
                .build();


        quiz = this.quizService.save(quiz);
        return QuizAttemptsResponse.builder()
                .failedAttempts(quiz.getAttempts())
                .status(status)
                .maxAttempts(3)
                .build();
    }

    private void inactivateCurrentQuiz(Quiz quiz) {
        if (Boolean.TRUE.equals(quiz.getOpened())) {
            quiz.setOpened(false);
        }
    }

}
