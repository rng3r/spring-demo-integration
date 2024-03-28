package ru.semenov.springdemointegration.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Data
public class BServiceMultiplyRequestDto {
  private BigDecimal firstNumber;
  private BigDecimal secondNumber;
  private BigDecimal thirdNumber;
}
