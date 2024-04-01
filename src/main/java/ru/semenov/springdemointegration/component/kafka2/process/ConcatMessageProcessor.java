package ru.semenov.springdemointegration.component.kafka2.process;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.produce.ResponseProducer;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.service.AService;

@RequiredArgsConstructor
@Component
public class ConcatMessageProcessor implements MessageProcessor<AServiceConcatRequestDto> {

  private final AService aService;
  private final ResponseProducer responseProducer;

  @Override
  public void process(Message<AServiceConcatRequestDto> message) {
    AServiceConcatRequestDto request = message.getPayload();
    AServiceConcatResponseDto response = aService.concat(request);
    responseProducer.produce(new GenericMessage<>(response, message.getHeaders()));
  }
}
