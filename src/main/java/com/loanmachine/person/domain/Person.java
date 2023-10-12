package com.loanmachine.person.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Person {
  private String personalCode;
  private Segment segment;
}
