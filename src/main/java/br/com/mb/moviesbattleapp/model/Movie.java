package br.com.mb.moviesbattleapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "imdb_id")
    private String imdbID;
    private String title;
    @Column(name = "mv_year")
    private String year;
    private String type;
    private String poster;
    private BigDecimal score;
}
