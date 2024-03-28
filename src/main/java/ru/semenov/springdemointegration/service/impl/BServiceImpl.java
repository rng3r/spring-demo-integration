package ru.semenov.springdemointegration.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyResponseDto;
import ru.semenov.springdemointegration.service.BService;

@Service
@Slf4j
public class BServiceImpl implements BService {

  @Override
  public BServiceAddResponseDto add(BServiceAddRequestDto bServiceRequestDto) {
    log.info("call add for {}", bServiceRequestDto);
    return new BServiceAddResponseDto(
          bServiceRequestDto.getFirstNumber()
              .add(bServiceRequestDto.getSecondNumber())
              .add(bServiceRequestDto.getThirdNumber())
    );
  }

  @Override
  public BServiceMultiplyResponseDto multiply(BServiceMultiplyRequestDto bServiceRequestDto) {
    log.info("call multiply for {}", bServiceRequestDto);
    return new BServiceMultiplyResponseDto(
        bServiceRequestDto.getFirstNumber()
            .multiply(bServiceRequestDto.getSecondNumber())
            .multiply(bServiceRequestDto.getThirdNumber())
    );
  }

}
