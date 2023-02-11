package br.com.mb.moviesbattleapp.model;

import br.com.mb.moviesbattleapp.model.security.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer position;

    @Column(name = "total_hits")
    private Integer totalHits;

    @Column(name = "total_quizzes")
    private Integer totalQuizzes;

    private BigDecimal points;

    @OneToOne
    private UserInfo player;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ranking_quiz", joinColumns = @JoinColumn(name = "ranking_id"),
            inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    private List<Quiz> quizzes;

}
