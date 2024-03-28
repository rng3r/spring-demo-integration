package ru.semenov.springdemointegration.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;
import ru.semenov.springdemointegration.service.AService;

@Service
@Slf4j
public class AServiceImpl implements AService {

  @Override
  public AServiceConcatResponseDto concat(AServiceConcatRequestDto aServiceRequestDto) {
    log.info("call concat for {}", aServiceRequestDto);
    return new AServiceConcatResponseDto(aServiceRequestDto.getFieldA() + aServiceRequestDto.getFieldB());
  }

  @Override
  public AServiceJoinResponseDto join(AServiceJoinRequestDto aServiceRequestDto) {
    log.info("call join for {}", aServiceRequestDto);
    return new AServiceJoinResponseDto(
        String.join(";", aServiceRequestDto.getFieldA(), aServiceRequestDto.getFieldB())
    );
  }

}
