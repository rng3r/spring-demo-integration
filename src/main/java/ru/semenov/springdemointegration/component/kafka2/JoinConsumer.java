package ru.semenov.springdemointegration.component.kafka2;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;
import ru.semenov.springdemointegration.service.AService;

@Component
public class JoinConsumer implements MessageConsumer<AServiceJoinRequestDto, AServiceJoinResponseDto>{

  public JoinConsumer(PublishSubscribeChannel joinChannel, AService aService, PublishSubscribeChannel kafkaOutputChannel) {
    joinChannel.subscribe((message) -> handleMessage(
        (Message<AServiceJoinRequestDto>) message,
        aService::join,
        kafkaOutputChannel)
    );
  }

}
