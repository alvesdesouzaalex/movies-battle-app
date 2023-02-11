package br.com.mb.moviesbattleapp.controller;

import br.com.mb.moviesbattleapp.domain.ranking.RankingResponse;
import br.com.mb.moviesbattleapp.service.ranking.RankingService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(value = "spring")
    @Test
    void whenInvokeGetRanking_thenReturns200() throws Exception {

        List<RankingResponse> rankingResponseList = new ArrayList<>();
        when(service.getRanking()).thenReturn(rankingResponseList);

        mockMvc.perform(get("/api/v1/ranking")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(rankingResponseList)))
                .andExpect(status().isOk());
    }
}
