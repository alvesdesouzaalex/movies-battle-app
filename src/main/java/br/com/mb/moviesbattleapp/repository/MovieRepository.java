package br.com.mb.moviesbattleapp.repository;


import br.com.mb.moviesbattleapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query(nativeQuery = true, value = "select m.id from movie m where m.type = 'movie'")
    List<Integer> findAllByType();

    @Query(nativeQuery = true, value = "select m.* from movie m where m.type = 'movie' and m.id in (:ids)")
    List<Movie> findAllById(List<Integer> ids);

    Movie findByImdbID(String imdbId);
}
