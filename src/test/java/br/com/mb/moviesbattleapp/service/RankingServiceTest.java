package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.domain.ranking.RankingResponse;
import br.com.mb.moviesbattleapp.fixture.QuizFixture;
import br.com.mb.moviesbattleapp.fixture.RankingFixture;
import br.com.mb.moviesbattleapp.fixture.UserInfoFixture;
import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.Ranking;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.RankingRepository;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import br.com.mb.moviesbattleapp.service.ranking.RankingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RankingServiceTest {


    @TestConfiguration
    static class RankingServiceTestConfiguration {
        @Bean
        public RankingService rankingService() {
            return new RankingService();
        }
    }

    @Autowired
    RankingService service;

    @MockBean
    RankingRepository repository;
    @MockBean
    QuizService quizService;

    @Test
    public void findRankingUser_Success() {

        UserInfo userInfo = UserInfoFixture.userInfo("user1");

        Ranking ranking = RankingFixture.ranking(1, 7, userInfo, BigDecimal.valueOf(700));
        when(repository.findByPlayer(userInfo)).thenReturn(ranking);

        Ranking response = service.findByPlayer(userInfo);

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("points", equalTo(BigDecimal.valueOf(700))));
        assertThat(response, hasProperty("position", equalTo(1)));
    }


    @Test
    public void recordRanking_Update_Ranking_Success() {

        UserInfo userInfo = UserInfoFixture.userInfo("user1");

        Ranking ranking = RankingFixture.ranking(1, 2, userInfo, BigDecimal.valueOf(700));
        when(repository.findByPlayer(userInfo)).thenReturn(ranking);

        List<Quiz> quizzes = List.of(
                QuizFixture.getQuiz(1, new ArrayList<>(), 0, true, BigDecimal.valueOf(8), 7),
                QuizFixture.getQuiz(2, new ArrayList<>(), 3, false, BigDecimal.valueOf(8), 7)

        );
        when(quizService.findAllByPlayer(userInfo)).thenReturn(quizzes);

        service.recordRanking(userInfo);
        verify(repository, times(1)).save(any());
    }


    @Test
    public void recordRanking_When_Has_No_Ranking_Success() {

        UserInfo userInfo = UserInfoFixture.userInfo("user1");

        when(repository.findByPlayer(userInfo)).thenReturn(null);

        List<Quiz> quizzes = List.of(QuizFixture.getQuiz(1, new ArrayList<>(), 0, true, BigDecimal.valueOf(8), 7));
        when(quizService.findAllByPlayer(userInfo)).thenReturn(quizzes);

        service.recordRanking(userInfo);
        verify(repository, times(1)).save(any());

    }


    @Test
    public void getRanking() {

        UserInfo user1 = UserInfoFixture.userInfo("user1");
        UserInfo user2 = UserInfoFixture.userInfo("user2");

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(RankingFixture.ranking(1, 7, user1, BigDecimal.valueOf(700)));
        rankings.add(RankingFixture.ranking(2, 3, user2, BigDecimal.valueOf(500)));

        when(repository.findAll()).thenReturn(rankings);

        List<RankingResponse> response = service.getRanking();
        assertThat(response, notNullValue());
        assertThat(response.get(0), hasProperty("player", equalTo(user1.getName())));
        assertThat(response.get(0), hasProperty("position", equalTo(1)));
        assertThat(response.get(0), hasProperty("points", equalTo(BigDecimal.valueOf(700))));

        assertThat(response.get(1), hasProperty("player", equalTo(user2.getName())));
        assertThat(response.get(1), hasProperty("position", equalTo(2)));
        assertThat(response.get(1), hasProperty("points", equalTo(BigDecimal.valueOf(500))));

    }


    @Test
    public void generateRanking() {

        UserInfo user1 = UserInfoFixture.userInfo("user1");
        UserInfo user2 = UserInfoFixture.userInfo("user2");

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(RankingFixture.ranking(1, 7, user1, BigDecimal.valueOf(700)));
        rankings.add(RankingFixture.ranking(2, 3, user2, BigDecimal.valueOf(300)));

        when(repository.findAllBy()).thenReturn(rankings);

        service.generateRanking();
        verify(repository, times(2)).save(any(Ranking.class));

    }


}
