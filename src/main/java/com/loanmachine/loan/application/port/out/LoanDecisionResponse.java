package com.loanmachine.loan.application.port.out;

import lombok.Builder;

@Builder
public record LoanDecisionResponse(boolean decision, Double maxSum, Integer proposedPeriod) {
  public LoanDecisionResponse(boolean decision) {
    this(decision, null, null);
  }

  public LoanDecisionResponse(boolean decision, Double maxSum) {
    this(decision, maxSum, null);
  }
}
