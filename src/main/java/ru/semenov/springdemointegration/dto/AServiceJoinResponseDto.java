package ru.semenov.springdemointegration.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Data
public class AServiceJoinResponseDto {
  private final String result;
}
