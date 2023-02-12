package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.model.Ranking;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.util.Utils;

import java.math.BigDecimal;

public class RankingFixture {

    public static Ranking ranking(Integer position, Integer quantityCorrect, UserInfo userInfo, BigDecimal points) {
        int id = Utils.secureRandom().nextInt();
        return Ranking.builder()
                .id(id)
                .position(position)
                .totalHits(quantityCorrect)
                .player(userInfo)
                .points(points)
                .build();
    }
}
