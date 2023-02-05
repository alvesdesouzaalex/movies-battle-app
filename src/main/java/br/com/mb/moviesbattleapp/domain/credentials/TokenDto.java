package br.com.mb.moviesbattleapp.domain.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenDto {
    private String username;
    private String accessToken;
    private String refreshToken;
}
