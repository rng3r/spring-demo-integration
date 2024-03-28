package ru.semenov.springdemointegration.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.semenov.springdemointegration.component.kafka.AddTransformer;
import ru.semenov.springdemointegration.component.kafka.ConcatTransformer;
import ru.semenov.springdemointegration.component.kafka.JoinTransformer;
import ru.semenov.springdemointegration.component.kafka.MultiplyTransformer;
import ru.semenov.springdemointegration.message.Command;

@EnableIntegration
@Slf4j
@Configuration
@Profile("memory")
public class InMemoryIntegrationConfig {

  @Bean(name = "raw-command-input-channel")
  public DirectChannel rawCommandInputChannel() {
    return MessageChannels.direct().datatype(String.class).getObject();
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
  public IntegrationFlow inputToCommandFlow() {
    return IntegrationFlow
        .from(rawCommandInputChannel())
        .transform(Transformers.fromJson(Command.class))
        .enrichHeaders(h -> h.headerExpression("action", "payload.action"))
        .channel(commandInputChannel())
        .get();
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
        .handle(msg -> log.info("output channel recieve msg -> {}", msg))
        .get();
  }



  @Bean
  @InboundChannelAdapter(value = "raw-command-input-channel", poller = @Poller(fixedDelay = "1000"))
  public MessageSource<String> jsonStringSource() {
    return new MessageSource<String>() {
      @Override
      public Message<String> receive() {
        List<String> possibleActions = Arrays.asList("concat", "join", "add", "multiply");
        Collections.shuffle(possibleActions);
        String uuid = UUID.randomUUID().toString();
        String action = possibleActions.getFirst();

        String request = switch (action) {
          case "concat", "join" -> "{ \"fieldA\": \"a\", \"fieldB\": \"b\" }";
          case "add", "multiply" -> "{ \"firstNumber\": 2, \"secondNumber\": 3, \"thirdNumber\": 4 }";
          default -> "null";
        };



        String json = "{ \"uuid\": \"" + uuid + "\", \"action\": \"" + action + "\", \"request\": " + request + ", \"response\": null }";
        log.info("{}", json);
        return new GenericMessage<>(json);
      }
    };
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


}
