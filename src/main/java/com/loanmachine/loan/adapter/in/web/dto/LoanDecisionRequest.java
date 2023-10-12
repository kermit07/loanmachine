package com.loanmachine.loan.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record LoanDecisionRequest(@NotEmpty(message = "personalCode cannot be empty") String personalCode,
                                  @Min(value = 2000, message = "amount cannot be less than 2000")
                                  @Max(value = 10000, message = "amount cannot be greater than 10000")
                                  double amount,

                                  @Min(value = 12, message = "period cannot be less than 12")
                                  @Max(value = 60, message = "period cannot be greater than 60")
                                  int period) {
}
