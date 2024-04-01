package ru.semenov.springdemointegration.component.kafka2.consume;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.process.JoinMessageProcessor;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;

@Component
@RequiredArgsConstructor
public class JoinConsumer implements MessageConsumer<AServiceJoinRequestDto>{

  private final JoinMessageProcessor joinMessageProcessor;
  private final PublishSubscribeChannel joinChannel;

  @PostConstruct
  public void init() {
    joinChannel.subscribe((message) -> handleMessage(
        (Message<AServiceJoinRequestDto>) message,
        joinMessageProcessor)
    );
  }

}
