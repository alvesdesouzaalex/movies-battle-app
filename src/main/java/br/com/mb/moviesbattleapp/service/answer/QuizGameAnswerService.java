package br.com.mb.moviesbattleapp.service.answer;

import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.quiz.AttemptsConstants;
import br.com.mb.moviesbattleapp.model.quiz.QuizAttempts;
import br.com.mb.moviesbattleapp.repository.QuizRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizGameAnswerService {

    @Autowired
    private QuizRepository repository;

    @Autowired
    private MovieService movieService;

    public QuizBasic answerQuiz(QuizBasic request) {
        QuizDto quizDto = request.getMovies().stream().max(Comparator.comparing(QuizDto::getRate)).orElseThrow(RuntimeException::new);
        Movie movie = this.movieService.findByImdbID(quizDto.getImdbID());

        Map<String, Object> map = new HashMap<>();
        int score = 0;
        if (!Objects.equals(movie.getRate(), quizDto.getRate())) {
            QuizAttempts.attemptsFailed.put(AttemptsConstants.FIRST, map);
        } else {
            score = 1;
        }

        ObjectMapper mapper = new ObjectMapper();
        String attemptsFailed;
        try {
            attemptsFailed = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(QuizAttempts.attemptsFailed);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Quiz quiz = Quiz.builder()
                .attempts(1)
                .attemptsFailedMap(attemptsFailed)
                .rate(quizDto.getRate())
                .movies(List.of(movie))
                .opened(true)
                .score(score)
                .build();

        this.repository.save(quiz);
        return request;
    }

}
