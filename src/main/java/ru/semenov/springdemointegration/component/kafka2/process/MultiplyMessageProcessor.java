package ru.semenov.springdemointegration.component.kafka2.process;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.produce.ResponseProducer;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyResponseDto;
import ru.semenov.springdemointegration.service.BService;

@RequiredArgsConstructor
@Component
public class MultiplyMessageProcessor implements MessageProcessor<BServiceMultiplyRequestDto> {

  private final BService bService;
  private final ResponseProducer responseProducer;

  @Override
  public void process(Message<BServiceMultiplyRequestDto> message) {
    BServiceMultiplyRequestDto request = message.getPayload();
    BServiceMultiplyResponseDto response = bService.multiply(request);
    responseProducer.produce(new GenericMessage<>(response, message.getHeaders()));

  }
}
