package ru.semenov.springdemointegration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingKafkaListener {

//  @KafkaListenertener(
//      topics = {"input"},
//      containerFactory = "kafkaListenerContainerFactory",
//      autoStartup = "true"
//  )
  public void onMessage(ConsumerRecord<String, String> consumerRecord) {
    log.info("Message -> {}", consumerRecord);
  }

}
