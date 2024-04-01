package ru.semenov.springdemointegration.component.kafka2.process;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.produce.ResponseProducer;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;
import ru.semenov.springdemointegration.service.AService;

@Component
@RequiredArgsConstructor
public class JoinMessageProcessor implements MessageProcessor<AServiceJoinRequestDto> {

  private final AService aService;
  private final ResponseProducer responseProducer;

  @Override
  public void process(Message<AServiceJoinRequestDto> message) {
    AServiceJoinRequestDto request = message.getPayload();
    AServiceJoinResponseDto response = aService.join(request);
    responseProducer.produce(new GenericMessage<>(response, message.getHeaders()));
  }
}
