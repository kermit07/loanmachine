package com.loanmachine.loan.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerE2ETest {

  @Autowired
  private MockMvc mockMvc;

  private static final String bodyRequest = """
      {
            "personalCode": "%s",
            "amount": %d,
            "period": %d
      }
      """;

  @Test
  void shouldReturnCorrectResponse() throws Exception {
    mockMvc.perform(post("/loan")
            .content(bodyRequest.formatted("49002010965", 10000, 12))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(OK.value()))
        .andExpect(content().string("""
            {"decision":false,"maxSum":null,"proposedPeriod":null}"""));
  }

  @Test
  void shouldReturnNotFound() throws Exception {
    mockMvc.perform(post("/loan")
            .content(bodyRequest.formatted("1", 10000, 12))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(NOT_FOUND.value()));
  }

  @Test
  void shouldReturnBadRequest() throws Exception {
    mockMvc.perform(post("/loan")
            .content(bodyRequest.formatted("1", 1, 12))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(BAD_REQUEST.value()));
  }

}