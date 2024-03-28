package ru.semenov.springdemointegration.service;

import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyResponseDto;

public interface BService {

  BServiceAddResponseDto add(BServiceAddRequestDto bServiceRequestDto);
  BServiceMultiplyResponseDto multiply(BServiceMultiplyRequestDto bServiceRequestDto);

}
