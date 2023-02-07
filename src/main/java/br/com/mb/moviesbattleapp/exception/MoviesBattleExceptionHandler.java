package br.com.mb.moviesbattleapp.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;


@ControllerAdvice
public class MoviesBattleExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBussinessException(BusinessException ex, WebRequest request) {

        String userMessage = this.messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        String developerMessage = ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    public static class Erro {
        private String userMessage;
        private String developerMessage;

        public Erro(String userMessage, String developerMessage) {
            this.userMessage = userMessage;
            this.developerMessage = developerMessage;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }
    }
}



