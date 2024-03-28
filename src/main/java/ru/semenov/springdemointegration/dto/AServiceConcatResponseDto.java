package ru.semenov.springdemointegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Data
@AllArgsConstructor
public class AServiceConcatResponseDto {
  private String result;
}
