package ru.semenov.springdemointegration.service;

import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;

public interface AService {
  AServiceConcatResponseDto concat(AServiceConcatRequestDto aServiceRequestDto);
  AServiceJoinResponseDto join(AServiceJoinRequestDto aServiceRequestDto);
}
