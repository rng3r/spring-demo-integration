package ru.semenov.springdemointegration.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Data
public class AServiceConcatRequestDto {
  private String fieldA;
  private String fieldB;
}
