package br.com.mb.moviesbattleapp.repository;

import br.com.mb.moviesbattleapp.model.Ranking;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    Ranking findByPlayer(UserInfo player);

    @Query(name = "select r.* from ranking r order by r.quantity_correct desc;", nativeQuery = true)
    List<Ranking> findAllBy();
}
