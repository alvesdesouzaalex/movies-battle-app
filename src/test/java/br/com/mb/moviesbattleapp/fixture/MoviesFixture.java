package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.util.Utils;

import java.math.BigDecimal;

public class MoviesFixture {

    public static Movie movie(String title, String imdbID, String poster) {
        int id = Utils.secureRandom().nextInt(20);
        double rate = Utils.secureRandom().nextDouble(10);

        return Movie.builder()
                .id(id)
                .rate(BigDecimal.valueOf(rate))
                .year("2017")
                .title(title)
                .imdbID(imdbID)
                .poster(poster)
                .type("movie")
                .build();
    }
}
