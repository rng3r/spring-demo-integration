package ru.semenov.springdemointegration.component.kafka2.consume;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.process.AddMessageProcessor;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.service.BService;

@Component
@RequiredArgsConstructor
public class AddConsumer implements MessageConsumer<BServiceAddRequestDto>{

  private final AddMessageProcessor addMessageProcessor;
  private final PublishSubscribeChannel addChannel;

  @PostConstruct
  public void init() {
    addChannel.subscribe((message) -> handleMessage(
        (Message<BServiceAddRequestDto>) message,
        addMessageProcessor
    ));
  }

}
