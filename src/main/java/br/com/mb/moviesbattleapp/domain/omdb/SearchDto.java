package br.com.mb.moviesbattleapp.domain.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SearchDto implements Serializable {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    private String imdbID;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;

}
