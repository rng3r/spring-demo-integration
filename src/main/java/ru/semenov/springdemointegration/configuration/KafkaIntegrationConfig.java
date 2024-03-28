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
import org.springframework.integration.config.EnableIntegration;
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
import ru.semenov.springdemointegration.component.kafka.AddTransformer;
import ru.semenov.springdemointegration.component.kafka.ConcatTransformer;
import ru.semenov.springdemointegration.component.kafka.JoinTransformer;
import ru.semenov.springdemointegration.component.kafka.MultiplyTransformer;
import ru.semenov.springdemointegration.dto.AServiceConcatRequestDto;
import ru.semenov.springdemointegration.message.Command;

@EnableIntegration
@Configuration
@Profile("kafka")
@Slf4j
@EnableKafka
public class KafkaIntegrationConfig {

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
  public IntegrationFlow kafkaRawInputFlow()  {
    return IntegrationFlow.from(
            Kafka.inboundChannelAdapter(
                kafkaConsumerFactory(),
                new ConsumerProperties("input")
            ),
            e -> e.poller(Pollers.fixedDelay(1000)))
        .transform(Transformers.fromJson(Command.class))
        .enrichHeaders(h -> h.headerExpression("action", "payload.action"))
        .enrichHeaders(h -> h.headerExpression("uuid", "payload.uuid"))
        .channel(commandInputChannel())
        .get();
  }

  @Bean(name = "command-input-channel")
  public DirectChannel commandInputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }

  @Bean(name = "a-service-concat-input-channel")
  public DirectChannel aServiceConcatInputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }

  @Bean(name = "a-service-join-input-channel")
  public DirectChannel aServiceJoinInputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }

  @Bean(name = "b-service-add-input-channel")
  public DirectChannel bServiceAddInputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }

  @Bean(name = "b-service-multiply-input-channel")
  public DirectChannel bServiceMultiplyInputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }

  @Bean
  public DirectChannel outputChannel() {
    return MessageChannels.direct().datatype(Command.class).getObject();
  }


  @Bean
  public IntegrationFlow processCommandFlow() {
    return IntegrationFlow
        .from(commandInputChannel())
        .route(serviceChannelsRouter())
        .get();
  }

  @Bean
  public IntegrationFlow concatFlow(ConcatTransformer concatTransformer) {
    return IntegrationFlow.from(aServiceConcatInputChannel())
        .transform(Transformers.fromJson(AServiceConcatRequestDto.class))
        .transform(concatTransformer)
        .channel(outputChannel())
        .get();
  }

  @Bean
  public IntegrationFlow joinFlow(JoinTransformer joinTransformer) {
    return IntegrationFlow.from(aServiceJoinInputChannel())
        .transform(joinTransformer)
        .channel(outputChannel())
        .get();
  }

  @Bean
  public IntegrationFlow addFlow(AddTransformer addTransformer) {
    return IntegrationFlow.from(bServiceAddInputChannel())
        .transform(addTransformer)
        .channel(outputChannel())
        .get();
  }

  @Bean
  public IntegrationFlow multiplyFlow(MultiplyTransformer multiplyTransformer) {
    return IntegrationFlow.from(bServiceMultiplyInputChannel())
        .transform(multiplyTransformer)
        .channel(outputChannel())
        .get();
  }

  @Bean
  public IntegrationFlow outputFlow() {
    return IntegrationFlow.from(outputChannel())
        .handle(sendToKafkaHandler())
        .get();
  }

  @Bean
  public HeaderValueRouter serviceChannelsRouter() {
    HeaderValueRouter headerValueRouter = new HeaderValueRouter("action");
    headerValueRouter.setChannelMapping("concat", "a-service-concat-input-channel");
    headerValueRouter.setChannelMapping("join", "a-service-join-input-channel");
    headerValueRouter.setChannelMapping("add", "b-service-add-input-channel");
    headerValueRouter.setChannelMapping("multiply", "b-service-multiply-input-channel");
    return headerValueRouter;
  }

  @Bean
  public MessageHandler sendToKafkaHandler() {
    KafkaProducerMessageHandler<String, String> handler =
        new KafkaProducerMessageHandler<>(kafkaTemplate());
    handler.setTopicExpression(new LiteralExpression("output"));
    handler.setMessageKeyExpression(new LiteralExpression("someKey"));
    return handler;
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


}
