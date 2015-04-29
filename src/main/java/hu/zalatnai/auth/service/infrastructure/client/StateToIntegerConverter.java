package hu.zalatnai.auth.service.infrastructure.client;

import hu.zalatnai.sdk.service.domain.State;

import javax.persistence.AttributeConverter;

abstract class StateToIntegerConverter<T extends State> implements AttributeConverter<T, Integer> {
    @Override
    public Integer convertToDatabaseColumn(T state) {
        return state.getId();
    }

    @Override
    abstract public T convertToEntityAttribute(Integer integer);
}
