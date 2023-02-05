package br.com.mb.moviesbattleapp.model;

import br.com.mb.moviesbattleapp.domain.quiz.QuizDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "quiz_movie", joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;
    private Integer attempts;
    private Boolean opened;
    private Integer score;
    private BigDecimal rate;

    @Column(name = "attempts_failed_map")
    private String attemptsFailedMap;

    //Todo fazer a logica para pegar o usuario da sessao
//    private User user;

}
