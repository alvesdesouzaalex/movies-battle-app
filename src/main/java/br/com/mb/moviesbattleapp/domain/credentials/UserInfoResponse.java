package br.com.mb.moviesbattleapp.domain.credentials;

import br.com.mb.moviesbattleapp.model.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String name;
    private String email;
    private String status;

    public static UserInfoResponse of(UserInfo userInfo) {
        return UserInfoResponse.builder().email(userInfo.getEmail()).name(userInfo.getName()).status("created").build();
    }
}
