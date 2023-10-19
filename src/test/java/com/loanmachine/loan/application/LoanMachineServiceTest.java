package com.loanmachine.loan.application;

import com.loanmachine.loan.adapter.in.web.dto.LoanDecisionRequest;
import com.loanmachine.loan.application.exception.PersonNotFoundException;
import com.loanmachine.person.application.port.out.LoadPersonPort;
import com.loanmachine.person.domain.Person;
import com.loanmachine.person.domain.Segment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static com.loanmachine.person.domain.Segment.DEBT;
import static com.loanmachine.person.domain.Segment.SEGMENT_1;
import static com.loanmachine.person.domain.Segment.SEGMENT_2;
import static com.loanmachine.person.domain.Segment.SEGMENT_3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoanMachineServiceTest {

  private static final String personalCode = "1";

  @ParameterizedTest
  @MethodSource("provideArguments")
  void testDecision(Segment segment, double requestedAmount, int requestedPeriod, boolean expectedDecision, Double expectedAmount, Integer expectedPeriod) {
    var loadPersonPort = mock(LoadPersonPort.class);
    var service = new LoanMachineService(loadPersonPort);
    var request = new LoanDecisionRequest(personalCode, requestedAmount, requestedPeriod);
    when(loadPersonPort.getPerson(personalCode))
        .thenReturn(Optional.of(Person.builder().personalCode(personalCode).segment(segment).build()));

    var decision = service.getDecision(request);

    assertEquals(decision.decision(), expectedDecision);
    assertEquals(decision.maxSum(), expectedAmount);
    assertEquals(decision.proposedPeriod(), expectedPeriod);
  }

  @Test
  void shouldThrowPersonNotFoundException() {
    var loadPersonPort = mock(LoadPersonPort.class);
    var service = new LoanMachineService(loadPersonPort);
    var request = new LoanDecisionRequest("2", 2000d, 12);

    assertThrows(PersonNotFoundException.class, () -> service.getDecision(request));
  }

  private static Stream<Arguments> provideArguments() {
    return Stream.of(
        Arguments.of(DEBT, 2345, 24, false, null, null),
        Arguments.of(SEGMENT_1, 2000, 24, true, (double) 2400, null),
        Arguments.of(SEGMENT_1, 2400, 24, true, (double) 2400, null),
        Arguments.of(SEGMENT_1, 2400, 12, false, (double) 2400, 24),
        Arguments.of(SEGMENT_1, 2600, 12, false, (double) 2600, 26),
        Arguments.of(SEGMENT_1, 6001, 24, false, (double) 2400, null),
        Arguments.of(SEGMENT_1, 10000, 60, false, (double) 6000, null),
        Arguments.of(SEGMENT_2, 10000, 12, false, (double) 3600, null),
        Arguments.of(SEGMENT_3, 10000, 12, true, (double) 10000, null)
    );
  }

}