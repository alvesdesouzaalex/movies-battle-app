package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.credentials.AuthRequestDto;
import br.com.mb.moviesbattleapp.domain.credentials.TokenDto;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoRequest;
import br.com.mb.moviesbattleapp.domain.credentials.UserInfoResponse;
import br.com.mb.moviesbattleapp.service.security.JwtService;
import br.com.mb.moviesbattleapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationManager authenticationManager;


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


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new")
    public ResponseEntity<UserInfoResponse> addNewUser(@RequestBody UserInfoRequest request) {
        var result = service.createUser(request);
        return ResponseEntity.ok(result);
    }

}
