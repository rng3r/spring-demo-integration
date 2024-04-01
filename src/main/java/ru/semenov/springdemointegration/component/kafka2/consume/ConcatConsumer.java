package ru.semenov.springdemointegration.component.kafka2.consume;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.process.ConcatMessageProcessor;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;

@Component
@RequiredArgsConstructor
public class ConcatConsumer implements MessageConsumer<AServiceConcatRequestDto>, InitializingBean {

  private final ConcatMessageProcessor concatMessageProcessor;
  private final SubscribableChannel concatChannel;


  @Override
  public void afterPropertiesSet() {
    concatChannel.subscribe((message) -> handleMessage(
        (Message<AServiceConcatRequestDto>) message,
        concatMessageProcessor
    ));
  }
}
