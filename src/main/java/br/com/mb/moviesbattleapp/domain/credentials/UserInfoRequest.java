package br.com.mb.moviesbattleapp.domain.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
    private String name;
    private String email;
    private String password;
    private String roles;
}
