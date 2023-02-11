package br.com.mb.moviesbattleapp.service.ranking;

import br.com.mb.moviesbattleapp.domain.ranking.RankingResponse;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.Ranking;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.RankingRepository;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RankingService {

    @Autowired
    private RankingRepository repository;

    @Autowired
    private QuizService quizService;

    public Ranking findByPlayer(UserInfo userInfo) {
        return this.repository.findByPlayer(userInfo);
    }

    @Transactional
    public void recordRanking(UserInfo userInfo) {
        Ranking ranking = this.makeRanking(userInfo);
        this.save(ranking);
    }

    @Async
    public void generateRanking() {
        List<Ranking> rankings = this.repository.findAllBy();
        rankings.sort(Comparator.comparing(Ranking::getPoints).reversed());
        AtomicInteger position = new AtomicInteger(1);
        rankings.forEach(ranking -> {
            ranking.setPosition(position.get());
            position.getAndIncrement();
            this.save(ranking);
        });
    }

    private Ranking makeRanking(UserInfo userInfo) {
        Ranking ranking = this.findByPlayer(userInfo);
        List<Quiz> quizzes = this.quizService.findAllByPlayer(userInfo);

        if (null == ranking) {
            return Ranking.builder()
                    .position(0)
                    .player(userInfo)
                    .totalHits(this.getTotalHits(quizzes))
                    .totalQuizzes(quizzes.size())
                    .quizzes(quizzes)
                    .points(this.calculateTotalPoints(quizzes))
                    .build();

        }

        ranking.setTotalHits(this.getTotalHits(quizzes));
        ranking.setPoints(this.calculateTotalPoints(quizzes));
        ranking.setTotalQuizzes(quizzes.size());
        return ranking;
    }

    private BigDecimal calculateTotalPoints(List<Quiz> quizzes) {
        int totalQuizzes = quizzes.size();
        BigDecimal qtdTotalQuizzes = BigDecimal.valueOf(totalQuizzes);
        BigDecimal totalPoints = qtdTotalQuizzes.multiply(this.hitPercentages(quizzes));
        System.out.println(totalPoints);
        return totalPoints;
    }

    private BigDecimal hitPercentages(List<Quiz> quizzes) {
        int totalQuizzes = quizzes.size();
        int totalHits = this.getTotalHits(quizzes);

        double result = (totalHits/(double) totalQuizzes) * 100;

        return BigDecimal.valueOf(result);

    }

    private void save(Ranking ranking) {
        this.repository.save(ranking);
    }

    private Integer getTotalHits(List<Quiz> quizzes) {
        return quizzes.stream()
                .filter(quiz -> !quiz.getOpened())
                .map(Quiz::getScore)
                .toList()
                .stream().mapToInt(Integer::intValue)
                .sum();
    }

    public List<RankingResponse> getRanking() {
        List<Ranking> rankings = this.repository.findAll();
        rankings.sort(Comparator.comparing(Ranking::getPosition));

        List<RankingResponse> rankingResponses = new ArrayList<>();
        rankings.forEach(ranking ->
                rankingResponses.add(RankingResponse.builder()
                        .position(ranking.getPosition())
                        .points(ranking.getPoints())
                        .player(ranking.getPlayer().getName())
                        .build())
        );
        return rankingResponses;
    }
}
