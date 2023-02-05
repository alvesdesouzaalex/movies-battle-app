package br.com.mb.moviesbattleapp.domain.quiz;

import br.com.mb.moviesbattleapp.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuizDto {

    private String imdbID;
    private String title;
    private String year;
    private String poster;
    private BigDecimal rate;

    public static QuizDto of(Movie movie) {
        return QuizDto.builder()
                .imdbID(movie.getImdbID())
                .title(movie.getTitle())
                .year(movie.getYear())
                .poster(movie.getPoster())
                .rate(null)
                .build();
    }
}
