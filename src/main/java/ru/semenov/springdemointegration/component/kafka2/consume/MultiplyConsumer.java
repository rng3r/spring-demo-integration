package ru.semenov.springdemointegration.component.kafka2.consume;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.semenov.springdemointegration.component.kafka2.process.MultiplyMessageProcessor;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;

@Component
public class MultiplyConsumer implements MessageConsumer<BServiceMultiplyRequestDto>{

  public MultiplyConsumer(PublishSubscribeChannel multiplyChannel, MultiplyMessageProcessor multiplyMessageProcessor) {
    multiplyChannel.subscribe((message) -> handleMessage(
          (Message<BServiceMultiplyRequestDto>) message,
          multiplyMessageProcessor
        )
    );
  }

}
