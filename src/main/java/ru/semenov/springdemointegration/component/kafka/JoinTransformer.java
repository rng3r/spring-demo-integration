package ru.semenov.springdemointegration.component.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;
import ru.semenov.springdemointegration.message.Command;
import ru.semenov.springdemointegration.service.AService;

@Component
@RequiredArgsConstructor
public class JoinTransformer extends OneTypeTransformer<Command<AServiceJoinRequestDto, AServiceJoinResponseDto>> {

  private final ObjectMapper objectMapper;
  private final AService aService;

  @Override
  protected Command<AServiceJoinRequestDto, AServiceJoinResponseDto> transformPayload(
      Command<AServiceJoinRequestDto, AServiceJoinResponseDto> payload) {
    AServiceJoinRequestDto request = objectMapper.convertValue(payload.getRequest(), AServiceJoinRequestDto.class);
    AServiceJoinResponseDto response = aService.join(request);
    payload.setResponse(response);
    return payload;
  }
}
