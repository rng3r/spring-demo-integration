package ru.semenov.springdemointegration.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.dto.AServiceJoinRequestDto;
import ru.semenov.springdemointegration.dto.BServiceAddRequestDto;
import ru.semenov.springdemointegration.dto.BServiceMultiplyRequestDto;
import ru.semenov.springdemointegration.message.Command2;

@EnableIntegration
@Configuration
@Profile("kafka-2")
@Slf4j
@EnableKafka
public class KafkaIntegration2Config {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaBootstrapServer;

  @Value("${spring.kafka.consumer.group-id}")
  private String kafkaGroupId;

  @Bean
  public Map<String, Object> kafkaConsumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return props;
  }

  @Bean
  public ConsumerFactory<String, String> kafkaConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfigs());
  }

  @Bean(name = "kafkaListenerContainerFactory")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(kafkaConsumerFactory());
    return factory;
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaBootstrapServer);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    // set more properties
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public IntegrationFlow kafkaRawInputFlow()  {
    return IntegrationFlow.from(
            Kafka.inboundChannelAdapter(
                kafkaConsumerFactory(),
                new ConsumerProperties("input")
            ),
            e -> e.poller(Pollers.fixedDelay(1000)))
        .transform(Transformers.fromJson(Command2.class))
        .enrichHeaders(h -> h.headerExpression("action", "payload.action"))
        .enrichHeaders(h -> h.headerExpression("uuid", "payload.uuid"))
        .transform((GenericTransformer<Command2<?>, Object>) Command2::getPayload)
        .channel(commandInputChannel())
        .get();
  }



  @Bean(name = "command-input-channel")
  public DirectChannel commandInputChannel() {
    return MessageChannels.direct().getObject();
  }

  @Bean
  public IntegrationFlow processCommandFlow() {
    return IntegrationFlow
        .from(commandInputChannel())
        .route(serviceChannelsRouter())
        .get();
  }

  @Bean
  public HeaderValueRouter serviceChannelsRouter() {
    HeaderValueRouter headerValueRouter = new HeaderValueRouter("action");
    headerValueRouter.setChannelMapping("concat", "a-service-concat-input-raw-channel");
    headerValueRouter.setChannelMapping("join", "a-service-join-input-raw-channel");
    headerValueRouter.setChannelMapping("add", "b-service-add-input-raw-channel");
    headerValueRouter.setChannelMapping("multiply", "b-service-multiply-input-raw-channel");
    return headerValueRouter;
  }


  @Bean(name = "a-service-concat-input-raw-channel")
  public DirectChannel aServiceConcatInputRawChannel() {
    return MessageChannels.direct().getObject();
  }

  @Bean
  public SubscribableChannel concatChannel() {
    return MessageChannels.publishSubscribe().getObject();
  }

  @Bean
  public IntegrationFlow concatProcessFlow() {
    return IntegrationFlow.from(aServiceConcatInputRawChannel())
        .transform(Transformers.fromMap(AServiceConcatRequestDto.class))
        .channel(concatChannel())
        .get();
  }

  @Bean(name = "a-service-join-input-raw-channel")
  public DirectChannel aServiceJoinInputRawChannel() {
    return MessageChannels.direct().getObject();
  }

  @Bean
  public PublishSubscribeChannel joinChannel() {
    return MessageChannels.publishSubscribe().getObject();
  }

  @Bean
  public IntegrationFlow joinProcessFlow() {
    return IntegrationFlow.from(aServiceJoinInputRawChannel())
        .transform(Transformers.fromMap(AServiceJoinRequestDto.class))
        .channel(joinChannel())
        .get();
  }

  @Bean(name = "b-service-add-input-raw-channel")
  public DirectChannel bServiceAddInputRawChannel() {
    return MessageChannels.direct().getObject();
  }

  @Bean
  public PublishSubscribeChannel addChannel() {
    return MessageChannels.publishSubscribe().getObject();
  }

  @Bean
  public IntegrationFlow addProcessFlow() {
    return IntegrationFlow.from(bServiceAddInputRawChannel())
        .transform(Transformers.fromMap(BServiceAddRequestDto.class))
        .channel(addChannel())
        .get();
  }


  @Bean(name = "b-service-multiply-input-raw-channel")
  public DirectChannel bServiceMultiplyInputChannel() {
    return MessageChannels.direct().getObject();
  }

  @Bean
  public PublishSubscribeChannel multiplyChannel() {
    return MessageChannels.publishSubscribe().getObject();
  }

  @Bean
  public IntegrationFlow multiplyProcessFlow() {
    return IntegrationFlow.from(bServiceMultiplyInputChannel())
        .transform(Transformers.fromMap(BServiceMultiplyRequestDto.class))
        .channel(multiplyChannel())
        .get();
  }


  @Bean
  public PublishSubscribeChannel kafkaOutputChannel() {
    return MessageChannels.publishSubscribe().getObject();
  }

  @Bean
  public IntegrationFlow sendResponseToKafkaFlow() {
    return IntegrationFlow.from(kafkaOutputChannel())
        .handle(sendToKafkaHandler())
        .get();

  }

  @Bean
  public MessageHandler sendToKafkaHandler() {
    KafkaProducerMessageHandler<String, String> handler =
        new KafkaProducerMessageHandler<>(kafkaTemplate());
    handler.setTopicExpression(new LiteralExpression("output"));
    handler.setMessageKeyExpression(new LiteralExpression(""));
    return handler;
  }

}
