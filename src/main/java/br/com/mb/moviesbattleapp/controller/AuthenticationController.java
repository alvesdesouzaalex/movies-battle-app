package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.credentials.AuthRequestDto;
import br.com.mb.moviesbattleapp.domain.credentials.TokenDto;
import br.com.mb.moviesbattleapp.service.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth endpoint")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Operation(summary = "Method to make login")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> authentication(@RequestBody AuthRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            TokenDto tokenDto = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(tokenDto);
        } else {
            throw new UsernameNotFoundException("Invalid credentials!");
        }
    }

}
