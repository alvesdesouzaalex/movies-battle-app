package br.com.mb.moviesbattleapp.model.security;


import br.com.mb.moviesbattleapp.config.UserInfoUserDetails;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"password", "roles"})
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
        String roles = "ROLE_USER";
        Optional<? extends GrantedAuthority> first = request.getAuthorities().stream().findFirst();
        if (first.isPresent()) {
            GrantedAuthority grantedAuthority = first.get();
            roles = grantedAuthority.getAuthority();
        }
        return UserInfo.builder()
                .email(request.getEmail())
                .name(request.getUsername())
                .password(request.getPassword())
                .roles(roles)
                .id(request.getId())
                .build();
    }

}
