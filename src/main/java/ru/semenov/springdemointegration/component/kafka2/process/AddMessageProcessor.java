package ru.semenov.springdemointegration.component.kafka2.process;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.produce.ResponseProducer;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.service.BService;

@RequiredArgsConstructor
@Component
public class AddMessageProcessor implements MessageProcessor<BServiceAddRequestDto>{

  private final BService service;
  private final ResponseProducer responseProducer;

  @Override
  public void process(Message<BServiceAddRequestDto> message) {
    BServiceAddRequestDto request = message.getPayload();
    BServiceAddResponseDto response = service.add(request);
    responseProducer.produce(new GenericMessage<>(response, message.getHeaders()));
  }

}
