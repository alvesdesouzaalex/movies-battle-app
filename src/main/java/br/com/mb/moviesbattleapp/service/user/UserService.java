package br.com.mb.moviesbattleapp.service.user;

import br.com.mb.moviesbattleapp.config.UserInfoUserDetails;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoRequest;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoResponse;
import br.com.mb.moviesbattleapp.model.quiz.QuizAttempts;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.repository.UserInfoRepository;
import br.com.mb.moviesbattleapp.security.context.SecurityContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfoResponse createUser(UserInfoRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserInfo userInfo = UserInfo.of(request);
        userInfo = repository.save(userInfo);
        return UserInfoResponse.of(userInfo);
    }


    public UserInfo getLoggedUser() {
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContext.getPrincipal();
        return UserInfo.of(userDetails);
    }
}
