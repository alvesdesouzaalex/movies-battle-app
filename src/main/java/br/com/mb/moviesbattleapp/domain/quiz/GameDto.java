package br.com.mb.moviesbattleapp.domain.quiz;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameDto {
    private String imdbID;
    private String title;
    private String year;
    private String type;
    private String poster;
}
