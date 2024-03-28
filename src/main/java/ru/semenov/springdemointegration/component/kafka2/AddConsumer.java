package ru.semenov.springdemointegration.component.kafka2;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinResponseDto;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.service.AService;
import ru.semenov.springdemointegration.service.BService;

@Component
public class AddConsumer implements MessageConsumer<BServiceAddRequestDto, BServiceAddResponseDto>{

  public AddConsumer(PublishSubscribeChannel addChannel, BService bService, PublishSubscribeChannel kafkaOutputChannel) {
    addChannel.subscribe((message) -> handleMessage(
        (Message<BServiceAddRequestDto>) message,
        bService::add,
        kafkaOutputChannel)
    );
  }

}
