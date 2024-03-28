package ru.semenov.springdemointegration.component.kafka;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

public abstract class OneTypeTransformer<T> extends AbstractPayloadTransformer<T, T> {

}
