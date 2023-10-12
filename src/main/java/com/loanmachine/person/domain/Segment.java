package com.loanmachine.person.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Segment {

  DEBT(-1),
  SEGMENT_1(100),
  SEGMENT_2(300),
  SEGMENT_3(1000);

  private final int value;
}
