package br.com.mb.moviesbattleapp.domain.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenDto {
    private String username;
    private String accessToken;
    private Boolean authenticated;
    private Date expiration;
}
