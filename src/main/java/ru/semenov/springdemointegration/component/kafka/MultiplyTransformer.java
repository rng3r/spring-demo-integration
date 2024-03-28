package ru.semenov.springdemointegration.component.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyResponseDto;
import ru.semenov.springdemointegration.message.Command;
import ru.semenov.springdemointegration.service.BService;

@Component
@RequiredArgsConstructor
public class MultiplyTransformer extends OneTypeTransformer<Command<BServiceMultiplyRequestDto, BServiceMultiplyResponseDto>> {

  private final ObjectMapper objectMapper;
  private final BService bService;


  @Override
  protected Command<BServiceMultiplyRequestDto, BServiceMultiplyResponseDto> transformPayload(
      Command<BServiceMultiplyRequestDto, BServiceMultiplyResponseDto> payload) {
    BServiceMultiplyRequestDto request = objectMapper.convertValue(payload.getRequest(), BServiceMultiplyRequestDto.class);
    BServiceMultiplyResponseDto response = bService.multiply(request);
    payload.setResponse(response);
    return payload;
  }
}
