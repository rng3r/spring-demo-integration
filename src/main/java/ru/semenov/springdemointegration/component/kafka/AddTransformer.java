package ru.semenov.springdemointegration.component.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.message.Command;
import ru.semenov.springdemointegration.service.BService;

@Component
@RequiredArgsConstructor
public class AddTransformer extends OneTypeTransformer<Command<BServiceAddRequestDto, BServiceAddResponseDto>> {

  private final ObjectMapper objectMapper;
  private final BService bService;

  @Override
  protected Command<BServiceAddRequestDto, BServiceAddResponseDto> transformPayload(
      Command<BServiceAddRequestDto, BServiceAddResponseDto> payload) {
    BServiceAddRequestDto request = objectMapper.convertValue(payload.getRequest(), BServiceAddRequestDto.class);
    BServiceAddResponseDto response = bService.add(request);
    payload.setResponse(response);
    return payload;
  }
}
