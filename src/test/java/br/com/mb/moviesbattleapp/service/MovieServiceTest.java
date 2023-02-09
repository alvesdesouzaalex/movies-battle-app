package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.repository.MovieRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MovieServiceTest {


    @TestConfiguration
    static class MovieServiceTestConfiguration {
        @Bean
        public MovieService userService() {
            return new MovieService();
        }
    }

    @Autowired
    MovieService service;

    @MockBean
    MovieRepository repository;

    @MockBean
    OmdbClient omdbClient;

    @Test
    public void findMovie_Success() {
        Movie movie = getMovie(1);
        when(repository.findByImdbID(movie.getImdbID())).thenReturn(movie);
        Movie response = service.findByImdbID(movie.getImdbID());

        assertThat(response, notNullValue());
        assertThat(response, hasProperty("imdbID", equalTo(movie.getImdbID())));

    }

    @Test
    public void findBattle_Success() {
        List<Movie> movies = List.of(getMovie(1), getMovie(2));
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        List<Integer> idsResult = List.of(1,2);

        when(repository.findAllByType()).thenReturn(ids);
        when(repository.findAllById(idsResult)).thenReturn(movies);

        List<Movie> response = service.getABattle();

        assertThat(response, notNullValue());
        assertThat(response.size(), comparesEqualTo(movies.size()));

    }

    private Movie getMovie(Integer id) {
        return Movie.builder()
                .id(id)
                .imdbID("imdv87956").build();
    }

}
