package br.com.mb.moviesbattleapp.service.movie;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import br.com.mb.moviesbattleapp.domain.omdb.OmdbResponse;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class MovieService {

    @Autowired
    private OmdbClient omdbClient;


    //TODO temporario
    List<String> movies = List.of("Batman", "Matrix", "Poderoso", "Avatar", "Star Wars", "Top",
            "Transformers", "Missão", "Game", "Superman", "Avengers", "ultimo", "comeco");


    @Autowired
    private MovieRepository repository;

    //TODO simplificar
    private void saveMovies(OmdbResponse response) {
        response.getSearchDtos()
                .parallelStream()
                .forEach(searchDto -> {
                    Random rd = new Random();
                    var rate = rd.nextDouble(11);
                    var movie = Movie.builder()
                            .id(1)
                            .imdbID(searchDto.getImdbID())
                            .poster(searchDto.getPoster())
                            .title(searchDto.getTitle())
                            .type(searchDto.getType())
                            .year(searchDto.getYear())
                            .rate(new BigDecimal(rate))
                            .build();
                    this.repository.save(movie);
                });

    }

    private void loadMovies() {
        Random rd = new Random();
        rd.nextInt(movies.size() - 1);
        var name = movies.get(rd.nextInt(movies.size() - 1));
        OmdbResponse movies = this.omdbClient.getMovies(name, 1);
        this.saveMovies(movies);
    }

    @Async
    public CompletableFuture<List<Movie>> getCurrentBattle() {
        this.loadMovies();
        return CompletableFuture.completedFuture(this.repository.findAllById(this.getMoviesIds()));
    }

    private List<Integer> getMoviesIds() {
        Random rd = new Random();
        List<Integer> moviesIds = this.repository.findAllByType();
        Integer firstMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));
        moviesIds.remove(firstMovieId);
        Integer secondMovieId = moviesIds.get(rd.nextInt(moviesIds.size() - 1));

        return List.of(firstMovieId, secondMovieId);
    }

    public Movie findByImdbID(String imdbID) {
        return this.repository.findByImdbID(imdbID);
    }
}
