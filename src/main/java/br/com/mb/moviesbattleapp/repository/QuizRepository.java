package br.com.mb.moviesbattleapp.repository;

import br.com.mb.moviesbattleapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
