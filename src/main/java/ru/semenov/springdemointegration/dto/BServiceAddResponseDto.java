package ru.semenov.springdemointegration.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Data
public class BServiceAddResponseDto {
  private final BigDecimal result;
}
