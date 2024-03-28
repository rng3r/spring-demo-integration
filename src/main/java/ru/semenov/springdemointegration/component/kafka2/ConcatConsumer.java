package ru.semenov.springdemointegration.component.kafka2;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceConcatResponseDto;
import ru.semenov.springdemointegration.service.AService;

@Component
public class ConcatConsumer implements MessageConsumer<AServiceConcatRequestDto, AServiceConcatResponseDto> {

  public ConcatConsumer(SubscribableChannel concatChannel, AService aService, PublishSubscribeChannel kafkaOutputChannel) {
    concatChannel.subscribe((message) -> handleMessage(
        (Message<AServiceConcatRequestDto>) message,
        aService::concat,
        kafkaOutputChannel)
    );
  }

}
