package br.com.mb.moviesbattleapp.domain.ranking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RankingResponse {
    private Integer position;
    private BigDecimal points;
    private String player;
}
