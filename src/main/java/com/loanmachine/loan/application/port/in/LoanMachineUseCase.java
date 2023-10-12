package com.loanmachine.loan.application.port.in;

import com.loanmachine.loan.adapter.in.web.dto.LoanDecisionRequest;
import com.loanmachine.loan.application.port.out.LoanDecisionResponse;

public interface LoanMachineUseCase {

  LoanDecisionResponse getDecision(LoanDecisionRequest request);
}
