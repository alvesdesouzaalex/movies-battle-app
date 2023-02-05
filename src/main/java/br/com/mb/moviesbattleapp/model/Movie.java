package br.com.mb.moviesbattleapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "imdb_id", unique = true)
    private String imdbID;
    private String title;
    @Column(name = "mv_year")
    private String year;
    private String type;
    private String poster;
    private BigDecimal rate;
}
