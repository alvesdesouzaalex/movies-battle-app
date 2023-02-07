package br.com.mb.moviesbattleapp.repository;

import br.com.mb.moviesbattleapp.model.Quiz;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    Quiz findByUserInfo(UserInfo userInfo);

    Quiz findByUserInfoAndOpenedIsTrue(UserInfo userInfo);

    List<Quiz> findAllByUserInfo(UserInfo userInfo);
}
