package br.com.mb.moviesbattleapp.client;

import br.com.mb.moviesbattleapp.domain.omdb.OmdbResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "omdbapi", url = "https://www.omdbapi.com")
public interface OmdbClient {

    @GetMapping(value = "?apikey=30591db2&type=movie", produces = "application/json")
    OmdbResponse getMovies(@RequestParam(name = "s") String name, @RequestParam(name = "page") Integer page);
}

