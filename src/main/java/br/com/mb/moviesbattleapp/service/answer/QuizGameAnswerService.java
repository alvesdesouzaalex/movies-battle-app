package br.com.mb.moviesbattleapp.service.answer;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.ranking.RankingService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;

import static br.com.mb.moviesbattleapp.exception.MessageErrors.MOVIE_NOT_FOUND;

@Service
public class QuizGameAnswerService {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Transactional
    public QuizAttemptsResponse answerQuiz(QuizBasic request) {

        QuizDto quizDtoWinner = request.getMovies().stream().max(Comparator.comparing(QuizDto::getRate)).orElseThrow(RuntimeException::new);
        Quiz saved = this.quizService.findById(request.getId());
        Movie movieWinner = saved.getMovies()
                .stream().filter(movie -> Objects.equals(movie.getImdbID(), quizDtoWinner.getImdbID()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(MOVIE_NOT_FOUND));


        int attempts = saved.getAttempts();

        this.userService.validatePlayer(saved.getUserInfo());

        if (Objects.equals(movieWinner.getRate(), quizDtoWinner.getRate()) && Boolean.TRUE.equals(saved.getOpened())) {
            saved.setScore(1);
            this.inactivateCurrentQuiz(saved);
            this.processRanking(saved);

            return QuizAttemptsResponse.builder()
                    .failedAttempts(attempts)
                    .status("You win. Try a new Quiz.")
                    .maxAttempts(3)
                    .build();

        }

        attempts++;
        if (attempts >= 3 || Boolean.FALSE.equals(saved.getOpened())) {
            this.inactivateCurrentQuiz(saved);
            return QuizAttemptsResponse.builder()
                    .failedAttempts(attempts)
                    .status("Game over, ask for a new game")
                    .maxAttempts(3)
                    .build();
        }

        saved.setAttempts(attempts);
        this.quizService.save(saved);
        this.processRanking(saved);
        return QuizAttemptsResponse.builder()
                .failedAttempts(attempts)
                .status("You lose, try again.")
                .maxAttempts(3)
                .build();
    }

    private void processRanking(Quiz saved) {
        this.rankingService.recordRanking(saved.getUserInfo());
        this.rankingService.generateRanking();
    }


    private void inactivateCurrentQuiz(Quiz quiz) {
        if (Boolean.TRUE.equals(quiz.getOpened())) {
            quiz.setOpened(false);
        }
    }

}
