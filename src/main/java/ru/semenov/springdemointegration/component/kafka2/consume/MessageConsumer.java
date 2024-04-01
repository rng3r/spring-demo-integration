package ru.semenov.springdemointegration.component.kafka2.consume;

import java.util.function.Function;
import org.springframework.messaging.Message;
import ru.semenov.springdemointegration.component.kafka2.process.MessageProcessor;

public interface MessageConsumer<Input> {

  default void handleMessage(Message<Input> message, MessageProcessor<Input> processor) {
    processor.process(message);
  }



}
