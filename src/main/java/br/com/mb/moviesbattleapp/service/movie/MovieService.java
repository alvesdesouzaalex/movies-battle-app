package br.com.mb.moviesbattleapp.service.movie;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import br.com.mb.moviesbattleapp.domain.omdb.OmdbResponse;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.repository.MovieRepository;
import br.com.mb.moviesbattleapp.util.Utils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private static final List<String> movies = List.of("Batman", "Matrix", "Poderoso", "Avatar", "Star Wars", "Top",
            "Transformers", "Missão", "Game", "Superman", "Avengers", "ultimo", "comeco");

    @Autowired
    private OmdbClient omdbClient;

    @Autowired
    private MovieRepository repository;


    @Async
    public void loadMovies() {
        Random rd = Utils.secureRandom();
        var name = movies.get(rd.nextInt(movies.size() - 1));
        OmdbResponse moviesList = this.omdbClient.getMovies(name, 1);
        this.saveMovies(moviesList);
    }

    public Movie findByImdbID(String imdbID) {
        return this.repository.findByImdbID(imdbID);
    }

    public List<Movie> getABattle() {
        List<Integer> moviesIds = this.getMoviesIds();
        return this.repository.findAllById(moviesIds);
    }

    @Transactional
    private void saveMovies(OmdbResponse response) {
        response.getSearchDtos()
                .parallelStream()
                .forEach(searchDto -> {
                    Random rd = Utils.secureRandom();
                    var rate = rd.nextDouble(10);
                    var movie = Movie.builder()
                            .imdbID(searchDto.getImdbID())
                            .poster(searchDto.getPoster())
                            .title(searchDto.getTitle())
                            .type(searchDto.getType())
                            .year(searchDto.getYear())
                            .rate(BigDecimal.valueOf(rate))
                            .build();
                    try {
                        this.repository.saveAndFlush(movie);
                    } catch (DataIntegrityViolationException e) {
                        LOGGER.error(e.getMessage());
                    }
                });

    }

    private List<Integer> getMoviesIds() {
        Random rd = Utils.secureRandom();
        List<Integer> moviesIds = this.repository.findAllByType();
        Integer firstMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));
        moviesIds.remove(firstMovieId);
        Integer secondMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));

        return List.of(firstMovieId, secondMovieId);
    }

}
