package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.quiz.QuizAttemptsResponse;
import br.com.mb.moviesbattleapp.domain.quiz.QuizBasic;
import br.com.mb.moviesbattleapp.domain.quiz.QuizRequest;
import br.com.mb.moviesbattleapp.domain.quiz.QuizResponse;
import br.com.mb.moviesbattleapp.service.answer.QuizGameAnswerService;
import br.com.mb.moviesbattleapp.service.quiz.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService service;
    @MockBean
    private QuizGameAnswerService quizGameAnswerService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(value = "spring")
    @Test
    void whenInvokeCurrentQuiz_thenReturns200() throws Exception {

        QuizBasic response = QuizResponse.builder().id(1).movies(new ArrayList<>()).build();
        when(service.getCurrentQuiz()).thenReturn(response);

        mockMvc.perform(get("/api/v1/quiz")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    void whenInvokeAnswerQuiz_thenReturns200() throws Exception {

        QuizBasic request = QuizRequest.builder().id(1).movies(new ArrayList<>()).build();
        QuizAttemptsResponse quizAttemptsResponse = QuizAttemptsResponse.builder()
                .maxAttempts(3)
                .failedAttempts(0)
                .status("you win")
                .build();

        when(quizGameAnswerService.answerQuiz(request)).thenReturn(quizAttemptsResponse);

        mockMvc.perform(post("/api/v1/quiz")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(quizAttemptsResponse)))
                .andExpect(status().isOk());
    }
}
