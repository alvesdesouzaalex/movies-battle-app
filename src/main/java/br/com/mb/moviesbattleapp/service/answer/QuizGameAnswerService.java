package br.com.mb.moviesbattleapp.service.answer;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.quiz.AttemptsConstants;
import br.com.mb.moviesbattleapp.model.quiz.QuizAttempts;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.com.mb.moviesbattleapp.exception.MessageErrors.UNAUTHORIZED_USER;

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
        Movie movieWinner = this.movieService.findByImdbID(quizDtoWinner.getImdbID());

        Quiz saved = this.quizService.findBYid(request.getId());
        int attempts = saved.getAttempts();
        attempts++;

        this.userService.validatePrayer(saved.getUserInfo());

        if (Objects.equals(movieWinner.getRate(), quizDtoWinner.getRate()) && Boolean.TRUE.equals(saved.getOpened())) {
            saved.setScore(1);
            this.inactivateCurrentQuiz(saved);
            return QuizAttemptsResponse.builder()
                    .failedAttempts(attempts)
                    .status("You win. Try a new Quiz.")
                    .maxAttempts(3)
                    .build();

        }

        if (attempts >= 3 || Boolean.FALSE.equals(saved.getOpened())) {
            this.inactivateCurrentQuiz(saved);
            return QuizAttemptsResponse.builder()
                    .failedAttempts(attempts)
                    .status("Game over, ask for a new game")
                    .maxAttempts(3)
                    .build();
        }


        saved = this.quizService.save(saved);
        return QuizAttemptsResponse.builder()
                .failedAttempts(saved.getAttempts())
                .status("You lose, try again.")
                .maxAttempts(3)
                .build();
    }


    private void inactivateCurrentQuiz(Quiz quiz) {
        if (Boolean.TRUE.equals(quiz.getOpened())) {
            quiz.setOpened(false);
        }
    }

}
