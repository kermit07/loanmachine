package com.loanmachine.loan.application;

import com.loanmachine.loan.adapter.in.web.dto.LoanDecisionRequest;
import com.loanmachine.loan.application.exception.PersonNotFoundException;
import com.loanmachine.loan.application.port.in.LoanMachineUseCase;
import com.loanmachine.loan.application.port.out.LoanDecisionResponse;
import com.loanmachine.person.application.port.out.LoadPersonPort;
import com.loanmachine.person.domain.Person;
import com.loanmachine.person.domain.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoanMachineService implements LoanMachineUseCase {

  private static final double MIN_AMOUNT = 2000;
  private static final double MAX_AMOUNT = 10000;
  private static final int MIN_PERIOD = 12;
  private static final int MAX_PERIOD = 60;

  private final LoadPersonPort loadPersonPort;

  @Override
  public LoanDecisionResponse getDecision(LoanDecisionRequest request) {
    Person person = loadPersonPort.getPerson(request.personalCode()).orElseThrow(() -> new PersonNotFoundException(request.personalCode()));
    return calcDecision(person, request.amount(), request.period());
  }

  /**
   * Calculates the decision if the person can loan.
   * <p>
   * The formula for decision is: (credit_modifier / requested_amount) * requested_period
   * and the decision is positive only of the result is greater or equal to 1
   * <p>
   * For indebted person the response is no.
   * <p>
   * For acceptable requests the response is yes and the max possible amount.
   * To calc maximum sum that the person can loan we use the formula: credit_modifier * requested_period
   * <p>
   * For unacceptable requests the response is no, but we attach valid period proposal for the requested amount.
   * <p>
   *
   * @param person          personal data {@link Person}
   * @param requestedAmount requested loan amount
   * @param requestedPeriod requested loan period
   *
   * @return the decision {@link LoanDecisionResponse}
   */
  private LoanDecisionResponse calcDecision(Person person, double requestedAmount, int requestedPeriod) {
    if (person.getSegment() == Segment.DEBT) {
      return new LoanDecisionResponse(false);
    }
    var creditModifier = person.getSegment().getValue();
    var maxAmount = creditModifier * requestedPeriod;
    if (maxAmount >= MIN_AMOUNT) {
      return new LoanDecisionResponse(maxAmount >= requestedAmount, Math.min(maxAmount, MAX_AMOUNT));
    } else {
      var minValidPeriod = (int) Math.ceil(requestedAmount / creditModifier);
      if (minValidPeriod >= MIN_PERIOD && minValidPeriod <= MAX_PERIOD) {
        return new LoanDecisionResponse(false, requestedAmount, minValidPeriod);
      } else {
        return new LoanDecisionResponse(false);
      }
    }
  }

}
