package br.com.mb.moviesbattleapp.domain.movie;

import br.com.mb.moviesbattleapp.model.Movie;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MovieDto {
    private String imdbID;
    private String title;
    private String year;
    private String poster;
    private BigDecimal rate;

    public static MovieDto of(Movie movie) {
        return MovieDto.builder()
                .imdbID(movie.getImdbID())
                .title(movie.getTitle())
                .year(movie.getYear())
                .poster(movie.getPoster())
                .rate(null)
                .build();
    }
}
