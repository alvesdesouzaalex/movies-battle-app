package br.com.mb.moviesbattleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MoviesBattleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleAppApplication.class, args);
	}

}
