package br.com.mb.moviesbattleapp.domain.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OmdbResponse {

    @JsonProperty("Search")
    private List<SearchDto> searchDtos;
    private String totalResults;
    @JsonProperty("Response")
    private Boolean response;
}
