package ru.semenov.springdemointegration.component.kafka2;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddResponseDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyResponseDto;
import ru.semenov.springdemointegration.service.BService;

@Component
public class MultiplyConsumer implements MessageConsumer<BServiceMultiplyRequestDto, BServiceMultiplyResponseDto>{

  public MultiplyConsumer(PublishSubscribeChannel multiplyChannel, BService bService, PublishSubscribeChannel kafkaOutputChannel) {
    multiplyChannel.subscribe((message) -> handleMessage(
        (Message<BServiceMultiplyRequestDto>) message,
        bService::multiply,
        kafkaOutputChannel)
    );
  }

}
