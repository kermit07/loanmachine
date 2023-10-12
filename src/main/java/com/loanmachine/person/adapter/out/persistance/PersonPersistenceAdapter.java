package com.loanmachine.person.adapter.out.persistance;

import com.loanmachine.person.application.port.out.LoadPersonPort;
import com.loanmachine.person.domain.Person;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.loanmachine.person.domain.Segment.DEBT;
import static com.loanmachine.person.domain.Segment.SEGMENT_1;
import static com.loanmachine.person.domain.Segment.SEGMENT_2;
import static com.loanmachine.person.domain.Segment.SEGMENT_3;

@Component
public class PersonPersistenceAdapter implements LoadPersonPort {

  private final List<Person> people = List.of(
      Person.builder().personalCode("49002010965").segment(DEBT).build(),
      Person.builder().personalCode("49002010976").segment(SEGMENT_1).build(),
      Person.builder().personalCode("49002010987").segment(SEGMENT_2).build(),
      Person.builder().personalCode("49002010998").segment(SEGMENT_3).build()
  );

  @Override
  public Optional<Person> getPerson(@NonNull String personalCode) {
    return people.stream().filter(person -> personalCode.equals(person.getPersonalCode())).findFirst();
  }
}
