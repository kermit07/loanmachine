package com.loanmachine.loan.application.exception;

public class PersonNotFoundException extends RuntimeException {

  public PersonNotFoundException(String personalCode) {
    super(String.format("Person with code: %s not found", personalCode));
  }
}
