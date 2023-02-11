package br.com.mb.moviesbattleapp.security.context;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserLoggedSecurityContext {

    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
