package com.loanmachine.loan.adapter.in.web;

import com.loanmachine.loan.adapter.in.web.dto.LoanDecisionRequest;
import com.loanmachine.loan.application.LoanMachineService;
import com.loanmachine.loan.application.exception.PersonNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoanController.class)
class LoanControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoanMachineService service;

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
            .content(bodyRequest.formatted("1", 10000, 12))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(OK.value()));
  }

  @Test
  void shouldReturnNotFound() throws Exception {
    when(service.getDecision(any(LoanDecisionRequest.class))).thenThrow(PersonNotFoundException.class);

    mockMvc.perform(post("/loan")
            .content(bodyRequest.formatted("1", 10000, 12))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(NOT_FOUND.value()));
  }

  @ParameterizedTest
  @MethodSource("testValidationParameters")
  void shouldReturnBadRequest(String personalCode, int amount, int period) throws Exception {
    mockMvc.perform(post("/loan")
            .content(bodyRequest.formatted(personalCode, amount, period))
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().is(BAD_REQUEST.value()));
  }

  private static Stream<Arguments> testValidationParameters() {
    return Stream.of(
        Arguments.of("1", 1, 12),
        Arguments.of("1", 10001, 12),
        Arguments.of("1", 10000, 1),
        Arguments.of("1", 10000, 100),
        Arguments.of("", 10000, 12)
    );
  }

}