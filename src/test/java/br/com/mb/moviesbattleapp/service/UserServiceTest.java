package br.com.mb.moviesbattleapp.service;

import br.com.mb.moviesbattleapp.config.UserInfoUserDetails;
import br.com.mb.moviesbattleapp.exception.BusinessException;
import br.com.mb.moviesbattleapp.fixture.UserInfoFixture;
import br.com.mb.moviesbattleapp.model.security.UserInfo;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestConfiguration {
        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    UserService service;

    UserInfoUserDetails userDetails;

    @Before
    public void initSecurityContext() {
        userDetails = new UserInfoUserDetails(UserInfoFixture.userInfo("user1"));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

    }

    @Test
    public void getLoggedUser() {

        UserInfo response = service.getLoggedUser();
        assertThat(response, notNullValue());
        assertThat(response, hasProperty("name", equalTo(userDetails.getUsername())));
        assertThat(response, hasProperty("email", equalTo(userDetails.getEmail())));
        assertThat(response, hasProperty("password", equalTo(userDetails.getPassword())));
        assertThat(response, hasProperty("roles", equalTo(userDetails.getAuthorities().stream().findFirst().get().getAuthority())));

    }

    @Test
    public void validatePlayer_Success() {
        UserInfo userInfo = UserInfoFixture.userInfo("user1");
        service.validatePlayer(userInfo);

    }

    @Test(expected = BusinessException.class)
    public void validatePlayer_Unauthorized() {
        UserInfo userInfo = UserInfoFixture.userInfo("user2");
        service.validatePlayer(userInfo);

    }


}
