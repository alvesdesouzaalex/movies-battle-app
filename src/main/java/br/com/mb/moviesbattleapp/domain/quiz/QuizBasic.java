package br.com.mb.moviesbattleapp.domain.quiz;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuizBasic {
    protected List<QuizDto> movies;
}
