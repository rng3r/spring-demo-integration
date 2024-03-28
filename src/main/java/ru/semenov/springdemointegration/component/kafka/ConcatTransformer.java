package ru.semenov.springdemointegration.component.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.message.Command;
import ru.semenov.springdemointegration.service.AService;

@RequiredArgsConstructor
@Component
public class ConcatTransformer implements GenericTransformer<Command<AServiceConcatRequestDto, AServiceConcatResponseDto>, Command<AServiceConcatRequestDto, AServiceConcatResponseDto>> {

  private final ObjectMapper objectMapper;
  private final AService aService;

  @Override
  public Command<AServiceConcatRequestDto, AServiceConcatResponseDto> transform(Command<AServiceConcatRequestDto, AServiceConcatResponseDto> source) {
    AServiceConcatRequestDto aServiceConcatRequestDto = objectMapper.convertValue(source.getRequest(), AServiceConcatRequestDto.class);
    AServiceConcatResponseDto response = aService.concat(aServiceConcatRequestDto);
    source.setResponse(response);
    return source;
  }
}
