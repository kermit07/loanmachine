package com.loanmachine.person.application.port.out;

import com.loanmachine.person.domain.Person;

import java.util.Optional;

public interface LoadPersonPort {
  Optional<Person> getPerson(String personalCode);
}
