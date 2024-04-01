package ru.semenov.springdemointegration.component.kafka2.produce;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseProducer {

  private final PublishSubscribeChannel kafkaOutputChannel;

  public void produce(Message<?> message) {
    kafkaOutputChannel.send(message);
  }

}
