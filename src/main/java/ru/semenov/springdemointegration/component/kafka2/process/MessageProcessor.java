package ru.semenov.springdemointegration.component.kafka2.process;

import org.springframework.messaging.Message;

public interface MessageProcessor<T> {

  void process(Message<T> message);

}
