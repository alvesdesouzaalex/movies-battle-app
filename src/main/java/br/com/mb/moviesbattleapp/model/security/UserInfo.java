package br.com.mb.moviesbattleapp.model.security;


import br.com.mb.moviesbattleapp.config.UserInfoUserDetails;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String roles;

    public static UserInfo of(UserInfoRequest request) {
        return UserInfo.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .roles(request.getRoles())
                .build();
    }

    public static UserInfo of(UserInfoUserDetails request) {
        return UserInfo.builder()
                .email(request.getEmail())
                .name(request.getUsername())
                .password(request.getPassword())
                .id(request.getId())
                .build();
    }

}
