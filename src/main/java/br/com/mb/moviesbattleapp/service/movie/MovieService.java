package br.com.mb.moviesbattleapp.service.movie;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import br.com.mb.moviesbattleapp.domain.omdb.OmdbResponse;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class MovieService {

    @Autowired
    private OmdbClient omdbClient;

    //TODO temporario
    List<String> movies = List.of("Batman", "Matrix", "Poderoso", "Avatar", "Star Wars", "Top",
            "Transformers", "Missão", "Game", "Superman", "Avengers", "ultimo", "comeco");


    @Autowired
    private MovieRepository repository;


    @Async
    public void loadMovies() {
        Random rd = new Random();
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

    //TODO simplificar
    @Transactional
    private void saveMovies(OmdbResponse response) {
        response.getSearchDtos()
                .parallelStream()
                .forEach(searchDto -> {
                    Random rd = new Random();
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
                        System.out.println(e);
                    }
                });

    }

    private List<Integer> getMoviesIds() {
        Random rd = new Random();
        List<Integer> moviesIds = this.repository.findAllByType();
        Integer firstMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));
        moviesIds.remove(firstMovieId);
        Integer secondMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));

        return List.of(firstMovieId, secondMovieId);
    }

}
