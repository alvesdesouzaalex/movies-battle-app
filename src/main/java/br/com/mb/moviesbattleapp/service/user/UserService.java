package br.com.mb.moviesbattleapp.service.user;

import br.com.mb.moviesbattleapp.config.UserInfoUserDetails;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.security.context.UserLoggedSecurityContext;
import org.springframework.stereotype.Service;

import static br.com.mb.moviesbattleapp.exception.MessageErrors.UNAUTHORIZED_USER;

@Service
public class UserService {

    public UserInfo getLoggedUser() {
        UserInfoUserDetails userDetails = (UserInfoUserDetails) UserLoggedSecurityContext.getPrincipal();
        return UserInfo.of(userDetails);
    }

    public void validatePlayer(UserInfo userInfo) {
        UserInfo loggedUser = this.getLoggedUser();
        if (!loggedUser.equals(userInfo)) {
            throw new BusinessException(UNAUTHORIZED_USER);
        }
    }
}
