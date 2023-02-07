package br.com.mb.moviesbattleapp.service.ranking;

import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.Ranking;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.RankingRepository;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RankingService {

    @Autowired
    private RankingRepository repository;

    @Autowired
    private QuizService quizService;

    public Ranking findByPrayer(UserInfo userInfo) {
        return this.repository.findByPrayer(userInfo);
    }

    @Transactional
    public void recordRanking(UserInfo userInfo) {
        Ranking ranking = this.makeRanking(userInfo);
        assert ranking != null;
        this.save(ranking);
    }

    @Async
    public void generateRanking(){
        List<Ranking> rankings = this.repository.findAllBy();
        AtomicInteger position = new AtomicInteger(1);
        rankings.forEach(ranking -> {
            ranking.setPosition(position.get());
            position.getAndIncrement();
            this.save(ranking);
        });
    }

    private Ranking makeRanking(UserInfo userInfo) {
        Ranking ranking = this.findByPrayer(userInfo);
        List<Quiz> quizzes = this.quizService.findAllByPrayer(userInfo);

        if (null == ranking) {
            return Ranking.builder()
                    .position(0)
                    .prayer(userInfo)
                    .quantityCorrect(this.quantityCorrect(quizzes))
                    .quizzes(quizzes)
                    .build();

        }

        ranking.setQuantityCorrect(this.quantityCorrect(quizzes));
        return ranking;
    }

    private void save(Ranking ranking) {
        this.repository.saveAndFlush(ranking);
    }

    private Integer quantityCorrect(List<Quiz> quizzes) {
        return quizzes.stream()
                .filter(quiz -> !quiz.getOpened())
                .map(Quiz::getScore)
                .toList()
                .stream().mapToInt(Integer::intValue)
                .sum();
    }

}
