package ru.semenov.springdemointegration.component.kafka2;

import java.util.function.Function;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

public interface MessageConsumer<Input, Output> {

  default void handleMessage(Message<Input> message, Function<Input, Output> processMethod, PublishSubscribeChannel outputChannel) {
    Input requestPayload = message.getPayload();
    Output output = processMethod.apply(requestPayload);
    outputChannel.send(new GenericMessage<>(output, message.getHeaders()));
  }

}
