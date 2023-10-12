package com.loanmachine.loan.adapter.in.web;

import com.loanmachine.loan.adapter.in.web.dto.ErrorInfo;
import com.loanmachine.loan.adapter.in.web.dto.LoanDecisionRequest;
import com.loanmachine.loan.application.exception.PersonNotFoundException;
import com.loanmachine.loan.application.port.in.LoanMachineUseCase;
import com.loanmachine.loan.application.port.out.LoanDecisionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loan")
@Validated
@RequiredArgsConstructor
public class LoanController {

  private final LoanMachineUseCase loanMachineUseCase;

  @PostMapping
  public LoanDecisionResponse getLoan(@RequestBody @Valid LoanDecisionRequest request) {
    return loanMachineUseCase.getDecision(request);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorInfo handleError(HttpServletRequest req, MethodArgumentNotValidException exception) {
    return new ErrorInfo(req.getRequestURL().toString(), mapValidationExceptionToErrorMessages(exception).toString());
  }

  @ExceptionHandler(value = PersonNotFoundException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorInfo handleError(HttpServletRequest req, PersonNotFoundException exception) {
    return new ErrorInfo(req.getRequestURL().toString(), exception.getMessage());
  }

  private List<String> mapValidationExceptionToErrorMessages(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getAllErrors().stream()
        .filter(FieldError.class::isInstance)
        .map(FieldError.class::cast)
        .map(this::getFieldErrorStringFunction)
        .toList();
  }

  private String getFieldErrorStringFunction(FieldError fieldError) {
    return String.format("Invalid value of field '%s': '%s'", fieldError.getField(), fieldError.getDefaultMessage());
  }
}
