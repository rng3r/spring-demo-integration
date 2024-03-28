package ru.semenov.springdemointegration.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class BServiceAddRequestDto {
  private BigDecimal firstNumber;
  private BigDecimal secondNumber;
  private BigDecimal thirdNumber;
}
