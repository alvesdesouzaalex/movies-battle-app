package br.com.mb.moviesbattleapp.fixture;

import br.com.mb.moviesbattleapp.model.security.UserInfo;

public class UserInfoFixture {

    public static UserInfo userInfo(String userName) {
        return UserInfo.builder().name(userName).build();
    }
}
