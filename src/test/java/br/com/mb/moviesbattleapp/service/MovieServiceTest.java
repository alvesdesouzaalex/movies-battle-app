package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.client.OmdbClient;
import br.com.mb.moviesbattleapp.domain.omdb.OmdbResponse;
import br.com.mb.moviesbattleapp.domain.omdb.SearchDto;
import br.com.mb.moviesbattleapp.fixture.SearchDtoFixture;
import br.com.mb.moviesbattleapp.model.Movie;
import br.com.mb.moviesbattleapp.repository.MovieRepository;
import br.com.mb.moviesbattleapp.service.movie.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MovieServiceTest {

    @TestConfiguration
    static class MovieServiceTestConfiguration {
        @Bean
        public MovieService movieService() {
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
    public void loadMovies() {

        OmdbResponse omdbResponse = new OmdbResponse();
        omdbResponse.setResponse(true);
        omdbResponse.setTotalResults("5");
        omdbResponse.setSearchDtos(this.getSearchDtoList());

        List<String> moviesList = new ArrayList<>() ;
        moviesList.add("movie1");
        when(omdbClient.getMovies(Mockito.any(), anyInt())).thenReturn(omdbResponse);

        service.loadMovies(moviesList);
        verify(repository, times(1)).saveAllAndFlush(any());

    }

    @Test
    public void loadMovies_DataIntegrityViolationException() {

        OmdbResponse omdbResponse = new OmdbResponse();
        omdbResponse.setResponse(true);
        omdbResponse.setTotalResults("5");
        omdbResponse.setSearchDtos(this.getSearchDtoList());

        List<String> moviesList = new ArrayList<>() ;
        moviesList.add("movie1");

        when(repository.countAllByType("movie")).thenReturn(0);
        when(omdbClient.getMovies(Mockito.any(), anyInt())).thenReturn(omdbResponse);
        when(repository.saveAllAndFlush(any())).thenThrow(DataIntegrityViolationException.class);

        service.loadMovies(moviesList);
        verify(repository, times(1)).saveAllAndFlush(any());

    }


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

        List<Integer> idsResult = List.of(1, 2);

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

    private List<SearchDto> getSearchDtoList() {
        List<SearchDto> searchDtoList = new ArrayList<>();
        searchDtoList.add(SearchDtoFixture.of("imdbId1", "poster1", "title1", "2007"));
        return searchDtoList;
    }

}
