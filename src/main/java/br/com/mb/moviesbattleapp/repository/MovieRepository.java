package br.com.mb.moviesbattleapp.repository;


import br.com.mb.moviesbattleapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
