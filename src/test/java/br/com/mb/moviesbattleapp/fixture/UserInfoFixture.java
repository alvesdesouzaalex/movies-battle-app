package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.model.security.UserInfo;

public class UserInfoFixture {

    public static UserInfo userInfo() {
        return UserInfo.builder().name("user1").build();
    }
}
