package br.com.mb.moviesbattleapp.domain.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizAttemptsResponse {
    private Integer failedAttempts;
    private Integer maxAttempts;
    private String status;
}
