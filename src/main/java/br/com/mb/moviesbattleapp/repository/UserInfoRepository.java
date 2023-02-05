package br.com.mb.moviesbattleapp.repository;

import br.com.mb.moviesbattleapp.model.security.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

}
