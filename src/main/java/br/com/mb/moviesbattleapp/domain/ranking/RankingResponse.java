package br.com.mb.moviesbattleapp.domain.ranking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingResponse {
    private Integer position;
    private Integer points;
    private String player;
}
